package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.kneading.*;
import com.server.esgcafe.domain.entity.*;
import com.server.esgcafe.domain.enum_class.ItemType;
import com.server.esgcafe.domain.enum_class.KneadingGrade;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KneadingService {

    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final UserUnlockedRecipeRepository userUnlockedRecipeRepository;
    private final UserInventoryRepository userInventoryRepository;
    private final KneadingAttemptRepository kneadingAttemptRepository;
    private final UserInventoryService userInventoryService;

    @Transactional
    public KneadingStartResponse startKneading(KneadingStartRequest request) {
        if (request == null || isBlank(request.getNickname()) || isBlank(request.getFoodName())) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Food food = foodRepository.findByName(request.getFoodName())
                .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

        validateRecipe(user, food);
        findRequiredIngredients(food, userInventoryRepository.findByUser(user));

        KneadingAttempt attempt = kneadingAttemptRepository.save(
                KneadingAttempt.builder().user(user).food(food).build());
        return new KneadingStartResponse(attempt.getAttemptNo(), food.getName());
    }

    @Transactional
    public KneadingResultResponse finishKneading(KneadingResultRequest request) {
        if (request == null || isBlank(request.getNickname())
                || request.getAttemptNo() == null || request.getAttemptNo() <= 0) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        KneadingGrade grade = parseGrade(request.getGrade());

        // 같은 유저의 결과 요청을 직렬화해서 재전송과 동시 지급을 막는다.
        User user = userRepository.findByNickNameForUpdate(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        KneadingAttempt attempt = kneadingAttemptRepository.findByAttemptNoAndUser(request.getAttemptNo(), user)
                .orElseThrow(() -> new AppException(ErrorCode.KNEADING_ATTEMPT_NOT_FOUND));
        Food food = attempt.getFood();

        if (attempt.getGrade() != null && attempt.getGrade() != grade) {
            throw new AppException(ErrorCode.KNEADING_RESULT_CONFLICT);
        }

        List<UserInventory> inventory = new ArrayList<>(userInventoryRepository.findByUserForUpdate(user));
        if (attempt.getGrade() == null) {
            validateRecipe(user, food);
            findRequiredIngredients(food, inventory).forEach(UserInventory::subtractInventoryCount);

            if (grade.getDoughCount() > 0) {
                grantDough(user, food, grade.getDoughCount(), inventory);
            }
            attempt.complete(grade);
        }

        return new KneadingResultResponse(attempt.getAttemptNo(), food.getName(),
                grade, grade.getDoughCount(), userInventoryService.createInventoryResponse(inventory, true));
    }

    private void validateRecipe(User user, Food food) {
        if (!userUnlockedRecipeRepository.existsByUserAndFood(user, food)) {
            throw new AppException(ErrorCode.RECIPE_NOT_UNLOCKED);
        }
        if (food.getRecipes().isEmpty()) {
            throw new AppException(ErrorCode.KNEADING_RECIPE_NOT_FOUND);
        }
    }

    private Map<UserInventory, Integer> findRequiredIngredients(Food food, List<UserInventory> inventory) {
        Map<UserInventory, Integer> required = new LinkedHashMap<>();
        for (FoodRecipe recipe : food.getRecipes()) {
            if (recipe.getIngredient() == null || recipe.getQuantity() <= 0) {
                throw new AppException(ErrorCode.KNEADING_RECIPE_NOT_FOUND);
            }
            Long ingredientNo = recipe.getIngredient().getIngredientNo();
            UserInventory ingredient = inventory.stream()
                    .filter(item -> item.getItemType() == ItemType.INGREDIENT
                            && ingredientNo.equals(item.getFoodOrIngredientNo()))
                    .findFirst()
                    .orElseThrow(() -> new AppException(ErrorCode.INSUFFICIENT_INGREDIENTS));
            int quantity = required.merge(ingredient, recipe.getQuantity(), Integer::sum);
            if (ingredient.getCount() < quantity) {
                throw new AppException(ErrorCode.INSUFFICIENT_INGREDIENTS);
            }
        }
        return required;
    }

    private void grantDough(User user, Food food, int count, List<UserInventory> inventory) {
        UserInventory dough = inventory.stream()
                .filter(item -> item.getItemType() == ItemType.DOUGH
                        && food.getFoodNo().equals(item.getFoodOrIngredientNo()))
                .findFirst()
                .orElse(null);

        if (dough == null) {
            inventory.add(userInventoryRepository.save(UserInventory.builder()
                    .user(user)
                    .foodOrIngredientNo(food.getFoodNo())
                    .itemType(ItemType.DOUGH)
                    .count(count)
                    .build()));
            return;
        }

        dough.addInventoryCount(count);
    }

    private KneadingGrade parseGrade(String grade) {
        if (isBlank(grade)) {
            throw new AppException(ErrorCode.INVALID_KNEADING_GRADE);
        }
        try {
            return KneadingGrade.valueOf(grade);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_KNEADING_GRADE);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
