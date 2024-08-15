package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.food.FoodCheckRequest;
import com.server.esgcafe.domain.dto.food.FoodCheckResponse;
import com.server.esgcafe.domain.dto.food.FoodIngredientDTO;
import com.server.esgcafe.domain.dto.food.UserRewardDTO;
import com.server.esgcafe.domain.entity.Food;
import com.server.esgcafe.domain.entity.FoodIngredient;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserReward;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.FoodRepository;
import com.server.esgcafe.repository.UserRepository;
import com.server.esgcafe.repository.UserRewardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    private final UserRewardRepository userRewardRepository;

    public FoodCheckResponse checkUserCanMakeFood(FoodCheckRequest request) {

        log.info("🥐checkUserCanMakeFood 시작 - 닉네임: {}, 음식 이름: {}", request.getNickname(), request.getFoodName());

        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Food food = foodRepository.findByName(request.getFoodName())
                .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

        List<FoodIngredient> foodIngredients = food.getFoodIngredients();
        List<UserReward> userRewards = userRewardRepository.findByUser(user);

        for (FoodIngredient ingredient : foodIngredients) {

            int requiredQuantity = ingredient.getQuantity();
            String ingredientName = ingredient.getIngredient().getName();

            UserReward userReward = userRewards.stream()
                    .filter(reward -> reward.getRewardName().equals(ingredientName))
                    .findFirst()
                    .orElse(null);

            if (userReward == null || userReward.getRewardCount() < requiredQuantity) {
                return FoodCheckResponse.cannotMake("재료가 부족하여 빵을 만들 수 없습니다.");
            }
        }

        List<FoodIngredientDTO> foodIngredientDTOs = foodIngredients.stream()
                .map(fi -> new FoodIngredientDTO(fi.getIngredient().getName(), fi.getQuantity()))
                .collect(Collectors.toList());

        List<UserRewardDTO> userRewardDTOs = userRewards.stream()
                .map(ur -> new UserRewardDTO(ur.getRewardName(), ur.getRewardCount()))
                .collect(Collectors.toList());

        return FoodCheckResponse.canMake("빵을 만들 수 있습니다.", foodIngredientDTOs, userRewardDTOs);
    }
}
