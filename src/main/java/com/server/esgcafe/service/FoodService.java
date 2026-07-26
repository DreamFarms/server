package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.food.*;
import com.server.esgcafe.domain.entity.*;
import com.server.esgcafe.domain.enum_class.ItemType;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    private final UserInventoryRepository userInventoryRepository;

    public FoodCheckResponse checkUserCanMakeFood(FoodCheckRequest request) {

        log.info("🍞 checkUserCanMakeFood 시작 - 닉네임: {}, 음식 이름: {}", request.getNickname(), request.getFoodName());

        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Food food = foodRepository.findByName(request.getFoodName())
                .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

        List<UserInventory> userInventories = userInventoryRepository.findByUser(user);
        List<FoodIngredientDTO> foodIngredientDTOs = new ArrayList<>();

        // 음식의 모든 레시피를 확인
        for (FoodRecipe recipe : food.getRecipes()) {
            Ingredient ingredient = recipe.getIngredient();
            int requiredQuantity = recipe.getQuantity();
            String ingredientName = ingredient.getName();

            // RemainingIngredient에 맞춰 비교하는 부분
            UserInventory userInventory = userInventories.stream()
                    .filter(inventory -> inventory.getItemType() == ItemType.INGREDIENT &&
                            inventory.getFoodOrIngredientNo().equals(ingredient.getIngredientNo())) // ingredientNo로 비교
                    .findFirst()
                    .orElse(null);

            log.info("🍞 검사 중인 재료 - 이름: {}, 필요한 개수: {}, 유저가 가진 개수: {}",
                    ingredientName, requiredQuantity,
                    userInventory != null ? userInventory.getCount() : 0);

            if (userInventory == null || userInventory.getCount() < requiredQuantity) {
                FoodCheckResponse response = FoodCheckResponse.cannotMake("재료가 부족하여 빵을 만들 수 없습니다.");
                log.info("🍞 checkUserCanMakeFood 실패 - 닉네임: {}, 메시지: {}", request.getNickname(), response.getMessage());

                return response;
            }

            // 필요한 재료 DTO 추가
            foodIngredientDTOs.add(new FoodIngredientDTO(ingredientName, requiredQuantity));
        }

        List<UserRewardDTO> userRewardDTOs = userInventories.stream()
                .filter(inventory -> inventory.getItemType() == ItemType.INGREDIENT)
                .map(inventory -> new UserRewardDTO(inventory.getFoodOrIngredientNo().toString(), inventory.getCount()))
                .collect(Collectors.toList());

        FoodCheckResponse response = FoodCheckResponse.canMake("빵을 만들 수 있습니다.", foodIngredientDTOs, userRewardDTOs);
        log.info("🍞 checkUserCanMakeFood 성공 - 닉네임: {}, 메시지: {}", request.getNickname(), response.getMessage());
        log.info("🍞 필요한 재료: {}", foodIngredientDTOs);
        log.info("🍞 유저 보유 재료: {}", userRewardDTOs);

        return response;
    }

    @Transactional
    public FoodUpdateResponse updateUserRewardsAndBread(FoodUpdateRequest request) {

        log.info("🍞 userInventory 업데이트 및 리워드 차감 시작 - 요청: {}", request);

        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Food food = foodRepository.findByName(request.getFoodName())
                .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

        List<UserInventory> userInventories = userInventoryRepository.findByUser(user);

        // 몇 개를 만드는지 (값이 없거나 0이면 1개로 간주)
        int makeCount = Math.max(1, request.getBreadCount());

        // 레시피 기준으로 서버가 직접 차감
        for (FoodRecipe recipe : food.getRecipes()) {
            Ingredient ingredient = recipe.getIngredient();
            int needed = recipe.getQuantity() * makeCount;

            UserInventory userInventory = userInventories.stream()
                    .filter(inventory -> inventory.getItemType() == ItemType.INGREDIENT &&
                            inventory.getFoodOrIngredientNo().equals(ingredient.getIngredientNo()))
                    .findFirst()
                    .orElseThrow(() -> new AppException(ErrorCode.INSUFFICIENT_INGREDIENTS));

            if (userInventory.getCount() < needed) {
                log.warn("🍞 재료 부족 - {} 필요 {}, 보유 {}", ingredient.getName(), needed, userInventory.getCount());
                throw new AppException(ErrorCode.INSUFFICIENT_INGREDIENTS);
            }

            userInventory.updateCount(userInventory.getCount() - needed);
            userInventoryRepository.save(userInventory);
        }

        // 만든 빵을 유저 인벤토리에 추가 (이미 있으면 개수 증가, 없으면 새로 생성)
        UserInventory breadInventory = userInventories.stream()
                .filter(inventory -> inventory.getItemType() == ItemType.FOOD &&
                        inventory.getFoodOrIngredientNo().equals(food.getFoodNo()))
                .findFirst()
                .orElse(null);

        if (breadInventory != null) {
            breadInventory.addInventoryCount(makeCount);
            log.info("🍞 기존 빵 개수 증가 - name: {}, itemType: {}, foodNo: {}, +{}개 → 총 {}개",
                    food.getName(), ItemType.FOOD, food.getFoodNo(), makeCount, breadInventory.getCount());
        } else {
            breadInventory = UserInventory.builder()
                    .user(user)
                    .foodOrIngredientNo(food.getFoodNo())
                    .itemType(ItemType.FOOD)
                    .count(makeCount)
                    .build();
            log.info("🍞 신규 빵 인벤토리 생성 - name: {}, itemType: {}, foodNo: {}, {}개",
                    food.getName(), ItemType.FOOD, food.getFoodNo(), makeCount);
        }
        userInventoryRepository.save(breadInventory);
        log.info("🍞 userInventory 업데이트 및 리워드 차감 성공 (빵 {}개분)", makeCount);
        return new FoodUpdateResponse(true, LocalDateTime.now());
    }
}
