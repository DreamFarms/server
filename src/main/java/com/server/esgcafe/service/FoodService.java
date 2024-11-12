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

        log.info("🍞 userInventory 업데이트 및 리워드 차감 시작");

        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Food food = foodRepository.findByName(request.getFoodName())
                .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

        List<UserInventory> userInventories = userInventoryRepository.findByUser(user);

        try {
            // RemainingIngredient 리스트에서 재료 차감
            for (RemainingIngredient remainingIngredient : request.getRemainingIngredients()) {
                String ingredientName = remainingIngredient.getIngredientName();
                int remainingQuantity = remainingIngredient.getRemainingQuantity();

                // 재료의 ingredientNo를 찾아서 비교
                Ingredient ingredient = food.getRecipes().stream()
                        .filter(recipe -> recipe.getIngredient().getName().equals(ingredientName))
                        .map(FoodRecipe::getIngredient)
                        .findFirst()
                        .orElseThrow(() -> new AppException(ErrorCode.INGREDIENT_NOT_FOUND));

                UserInventory userInventory = userInventories.stream()
                        .filter(inventory -> inventory.getItemType() == ItemType.INGREDIENT &&
                                inventory.getFoodOrIngredientNo().equals(ingredient.getIngredientNo())) // ingredientNo로 비교
                        .findFirst()
                        .orElseThrow(() -> new AppException(ErrorCode.INSUFFICIENT_INGREDIENTS));

                // 재료 수량 차감
                if (userInventory.getCount() < remainingQuantity) {
                    throw new AppException(ErrorCode.INSUFFICIENT_INGREDIENTS);
                }

                userInventory.updateCount(userInventory.getCount() - remainingQuantity);
                userInventoryRepository.save(userInventory);
            }

            log.info("🍞 userInventory 업데이트 성공");

        } catch (Exception e) {
            // 예외 발생 시 롤백
            throw new RuntimeException("🍞 Failed to update user inventory", e);
        }

        // FoodUpdateResponse 생성
        FoodUpdateResponse response = new FoodUpdateResponse(true, LocalDateTime.now());
        log.info("🍞 userInventory 업데이트 및 리워드 차감 성공");

        return response;
    }
}
