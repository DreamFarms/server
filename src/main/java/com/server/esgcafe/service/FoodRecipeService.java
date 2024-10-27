package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.foodRecipe.*;
import com.server.esgcafe.domain.entity.*;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FoodRecipeService {

    private final UserRepository userRepository;
    private final UserRewardRepository userRewardRepository;
    private final UserUnlockedRecipeRepository userUnlockedRecipeRepository;
    private final FoodRecipeRepository recipeRepository;
    private final FoodRepository foodRepository;

    private final UserUnlockedRecipeService userUnlockedRecipeService;

    @Transactional(readOnly = true)
    public RecipeGameStartResponse startRecipeGame(String nickname) {

        User user = userRepository.findByNickName(nickname)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 유저의 보유 재료 정보 가져오기
        List<UserReward> userRewards = userRewardRepository.findByUser(user);

        // 유저가 해금한 레시피 정보 가져오기
        List<UserUnlockedRecipe> unlockedRecipes = userUnlockedRecipeRepository.findByUser(user);

        // 응답 DTO로 변환
        List<IngredientInfo> ingredientInfos = userRewards.stream()
                .map(reward -> new IngredientInfo(reward.getRewardName(), reward.getRewardCount()))
                .collect(Collectors.toList());

        List<UnlockedRecipeInfo> recipeInfos = unlockedRecipes.stream()
                .map(unlocked -> new UnlockedRecipeInfo(unlocked.getFood().getName()))
                .collect(Collectors.toList());

        // 보유 재료와 해금된 레시피 정보를 담아서 반환
        return new RecipeGameStartResponse(ingredientInfos, recipeInfos);
    }

    @Transactional
    public RecipeGuessResponse checkUserRecipe(UserRecipeGuessRequest request) {
        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 유저가 제출한 재료와 수량
        List<IngredientQuantity> userIngredients = request.getIngredients();

        // 모든 음식(레시피) 가져오기
        List<Food> allFoods = foodRepository.findAll();

        boolean result = false;
        String breadName = null;

        // 모든 음식의 레시피 확인
        for (Food food : allFoods) {
            // 해당 음식에 필요한 재료 목록 가져오기
            List<FoodRecipe> requiredIngredients = food.getRecipes();

            // 재료 비교 로직
            boolean allIngredientsMatch = requiredIngredients.stream().allMatch(required ->
                    userIngredients.stream().anyMatch(userIngredient ->
                            userIngredient.getIngredientName().equals(required.getIngredient().getName()) &&
                                    userIngredient.getQuantity() >= required.getQuantity()
                    )
            );

            if (allIngredientsMatch) {
                result = true;
                breadName = food.getName();

                // 해금된 레시피 저장
                userUnlockedRecipeService.saveUnlockedRecipe(user, food);
                break;
            }
        }

        return new RecipeGuessResponse(result, breadName);
    }
}
