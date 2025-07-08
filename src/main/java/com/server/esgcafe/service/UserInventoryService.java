package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.userInventory.UserInventoryInfo;
import com.server.esgcafe.domain.dto.userInventory.UserInventoryResponse;
import com.server.esgcafe.domain.dto.userInventory.UserInventorySaveRequest;
import com.server.esgcafe.domain.dto.userInventory.UserInventorySaveResponse;
import com.server.esgcafe.domain.entity.Food;
import com.server.esgcafe.domain.entity.Ingredient;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserInventory;
import com.server.esgcafe.domain.enum_class.ItemType;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.FoodRepository;
import com.server.esgcafe.repository.IngredientRepository;
import com.server.esgcafe.repository.UserInventoryRepository;
import com.server.esgcafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserInventoryService {

    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final IngredientRepository ingredientRepository;
    private final UserInventoryRepository userInventoryRepository;

    public List<UserInventoryResponse> getUserInventory(String nickname) {

        log.info("Inventory start");

        User user = userRepository.findByNickName(nickname)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<UserInventory> userInventories = userInventoryRepository.findByUser(user);

        List<UserInventoryResponse> response = new ArrayList<>();
        for (UserInventory inventory : userInventories) {
            Long itemNo = inventory.getFoodOrIngredientNo();
            int count = inventory.getCount();

            if (inventory.getItemType() == ItemType.FOOD) {
                Food food = foodRepository.findByFoodNo(itemNo)
                        .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));
                response.add(new UserInventoryResponse(food.getCode(), food.getName(), count));
            } else {
                Ingredient ingredient = ingredientRepository.findByIngredientNo(itemNo)
                        .orElseThrow(() -> new AppException(ErrorCode.INGREDIENT_NOT_FOUND));
                response.add(new UserInventoryResponse(ingredient.getCode(), ingredient.getName(), count));
            }
        }

        log.info("User inventory response: {}", response);
        return response;
    }

    @Transactional
    public UserInventorySaveResponse saveUserInventory(UserInventorySaveRequest request) {

        log.info("🍞 UserInventory 저장 시작");

        // 사용자 정보 확인
        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<UserInventoryInfo> inventoryInfos = request.getRewards();
        UserInventory lastInventory = null;

        // 아이템 순차적으로 처리
        for (int i = 0; i < inventoryInfos.size(); i++) {

            UserInventoryInfo inventoryInfo = inventoryInfos.get(i);

            log.info("🍞 처리 중인 인벤토리 아이템 - 순서: {}, 이름: {}", i + 1, inventoryInfo.getName());

            // Food 또는 Ingredient 인지 확인 후 해당 이름 찾기
            Long itemId;
            ItemType itemType; // 변경된 부분

            if (foodRepository.existsByName(inventoryInfo.getName())) {
                // Food 찾기
                Food food = foodRepository.findByName(inventoryInfo.getName())
                        .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));
                itemId = food.getFoodNo();
                itemType = ItemType.FOOD; // 변경된 부분
            } else {
                // Ingredient 찾기
                Ingredient ingredient = ingredientRepository.findByName(inventoryInfo.getName())
                        .orElseThrow(() -> new AppException(ErrorCode.INGREDIENT_NOT_FOUND));
                itemId = ingredient.getIngredientNo();
                itemType = ItemType.INGREDIENT; // 변경된 부분
            }

            // 기존 인벤토리 아이템이 있는지 확인
            UserInventory existingInventory = userInventoryRepository.findByUserAndFoodOrIngredientNoAndItemType(user, itemId, itemType)
                    .orElse(null);

            if (existingInventory != null) {
                // 기존 아이템의 수량 업데이트
                int updatedCount = existingInventory.getCount() + inventoryInfo.getCount();
                existingInventory.updateCount(updatedCount);
                lastInventory = userInventoryRepository.save(existingInventory); // 업데이트 후 저장
            } else {
                // 새 인벤토리 아이템 생성 후 저장
                lastInventory = userInventoryRepository.save(inventoryInfo.toEntity(user, itemId, itemType));
            }
        }

        // 응답 객체 생성
        boolean isSaved = lastInventory != null;
        LocalDateTime saveTime = isSaved ? lastInventory.getCreatedAt() : null;

        log.info("🍞 인벤토리 아이템 추가 완료");

        return new UserInventorySaveResponse(isSaved, saveTime);
    }

}