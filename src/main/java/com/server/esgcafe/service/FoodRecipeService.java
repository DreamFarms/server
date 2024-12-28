package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.foodRecipe.*;
import com.server.esgcafe.domain.entity.*;
import com.server.esgcafe.domain.enum_class.ItemType;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FoodRecipeService {

    private final UserRepository userRepository;
    private final UserUnlockedRecipeRepository userUnlockedRecipeRepository;
    private final FoodRecipeRepository recipeRepository;
    private final FoodRepository foodRepository;
    private final UserInventoryRepository userInventoryRepository;
    private final IngredientRepository ingredientRepository;

    private final UserUnlockedRecipeService userUnlockedRecipeService;

    @Transactional(readOnly = true)
    public RecipeGameStartResponse startRecipeGame(String nickname) {

        log.info("🍞Start recipe game start");

        User user = userRepository.findByNickName(nickname)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 유저의 보유 재료 정보 가져오기
        List<UserInventory> userInventories = userInventoryRepository.findByUser(user);

        // 모든 재료 정보 가져오기 (재료 번호를 이름과 매핑하기 위함)
        Map<Long, String> ingredientNameMap = ingredientRepository.findAll().stream()
                .collect(Collectors.toMap(Ingredient::getIngredientNo, Ingredient::getName));

        // 유저가 해금한 레시피 정보 가져오기
        List<UserUnlockedRecipe> unlockedRecipes = userUnlockedRecipeRepository.findByUserWithFood(user);

        // 응답 DTO로 변환
        List<IngredientInfo> ingredientInfos = userInventories.stream()
                .filter(inventory -> inventory.getItemType() == ItemType.INGREDIENT) // 필터링
                .map(inventory -> new IngredientInfo(
                        ingredientNameMap.get(inventory.getFoodOrIngredientNo()), // 이름 매핑
                        inventory.getCount()
                ))
                .collect(Collectors.toList());

        List<UnlockedRecipeInfo> recipeInfos = unlockedRecipes.stream()
                .map(unlocked -> new UnlockedRecipeInfo(
                        unlocked.getFood() != null ? unlocked.getFood().getName() : "Unknown"
                ))
                .collect(Collectors.toList());

        log.info("🍞 End recipe game start");

        // 보유 재료와 해금된 레시피 정보를 담아서 반환
        return new RecipeGameStartResponse(ingredientInfos, recipeInfos);
    }

    @Transactional
    public RecipeGuessResponse checkUserRecipe(UserRecipeGuessRequest request) {

        log.info("🍞 Check user recipe guess Start");

        log.info("🍞 Request received: {}", request);

        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 유저가 제출한 재료와 수량
        List<IngredientQuantity> userIngredients = request.getIngredients();

        // userIngredients가 null인지 확인
        if (userIngredients == null || userIngredients.isEmpty()) {
            throw new AppException(ErrorCode.MISSING_INGREDIENTS, "사용자가 제출한 재료 목록이 없습니다.");
        }

        log.info("🍞 User submitted ingredients: {}", userIngredients);

        // 모든 음식(레시피) 가져오기
        List<Food> allFoods = foodRepository.findAll();

        int resultState = 0;
        String breadName = null;

        // 인벤토리에서 재료 차감 (모든 경우)
        for (IngredientQuantity userIngredient : userIngredients) {
            String ingredientName = userIngredient.getIngredientName();
            int requiredQuantity = userIngredient.getQuantity();

            Ingredient ingredient = ingredientRepository.findByName(ingredientName)
                    .orElseThrow(() -> new AppException(ErrorCode.INGREDIENT_NOT_FOUND));

            UserInventory userInventory = userInventoryRepository.findByUserAndFoodOrIngredientNoAndItemType(
                            user, ingredient.getIngredientNo(), ItemType.INGREDIENT)
                    .orElseThrow(() -> new AppException(ErrorCode.INGREDIENT_NOT_FOUND));

            // 인벤토리에 재료가 충분한지 확인
            if (userInventory.getCount() < requiredQuantity) {
                throw new AppException(ErrorCode.INSUFFICIENT_INGREDIENTS,
                        String.format("재료가 부족합니다. 필요: %d, 보유: %d", requiredQuantity, userInventory.getCount()));
            }

            // 차감
            userInventory.subtractInventoryCount(requiredQuantity);
            userInventoryRepository.save(userInventory);
        }

        // 모든 음식의 레시피 확인
        for (Food food : allFoods) {
            // 해당 음식에 필요한 재료 목록 가져오기
            List<FoodRecipe> requiredIngredients = food.getRecipes();

            // 현재 확인 중인 음식과 필요한 재료 목록을 로그로 출력
            log.info("🍞 Checking recipe for food: {} with required ingredients: {}", food.getName(), requiredIngredients);

            // 재료 비교 로직
            boolean allIngredientsMatch = requiredIngredients.size() == userIngredients.size() &&
                    requiredIngredients.stream().allMatch(required ->
                            userIngredients.stream().anyMatch(userIngredient ->
                                    userIngredient.getIngredientName().equals(required.getIngredient().getName()) &&
                                            userIngredient.getQuantity() == required.getQuantity()
                            )
                    );

            if (allIngredientsMatch) {
                // 이미 해금된 레시피인지 확인
                boolean alreadyUnlocked = userUnlockedRecipeRepository.existsByUserAndFood(user, food);
                if (alreadyUnlocked) {
                    log.info("🍞 Already unlocked recipe : {}", food.getName());

                    RecipeGuessResponse response = new RecipeGuessResponse(-1, food.getName());
                    log.info("🍞 Final Response Object: {}", response);
                    return response;

//                    return new RecipeGuessResponse(null, food.getName());
                }

                // 레시피 해금
                breadName = food.getName();
                resultState = 1;

                // 해금된 레시피 저장하고 로그로 출력
                userUnlockedRecipeService.saveUnlockedRecipe(user, food);
                log.info("🍞 Recipe matched and unlocked for food: {}", breadName);
                break;
            } else {
                log.info("🍞 Recipe did not match for food: {}", food.getName());
            }
        }

        log.info("🍞 Check user recipe guess End - Bread Name: {}", breadName);

        return new RecipeGuessResponse(resultState, breadName);
    }

}
