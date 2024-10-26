package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.recipe.IngredientInfo;
import com.server.esgcafe.domain.dto.recipe.RecipeGameStartResponse;
import com.server.esgcafe.domain.dto.recipe.UnlockedRecipeInfo;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserReward;
import com.server.esgcafe.domain.entity.UserUnlockedRecipe;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.UserRepository;
import com.server.esgcafe.repository.UserRewardRepository;
import com.server.esgcafe.repository.UserUnlockedRecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecipeService {

    private final UserRepository userRepository;
    private final UserRewardRepository userRewardRepository;
    private final UserUnlockedRecipeRepository userUnlockedRecipeRepository;

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
                .map(unlocked -> new UnlockedRecipeInfo(unlocked.getRecipe().getFood().getName()))
                .collect(Collectors.toList());

        // 보유 재료와 해금된 레시피 정보를 담아서 반환
        return new RecipeGameStartResponse(ingredientInfos, recipeInfos);
    }
}
