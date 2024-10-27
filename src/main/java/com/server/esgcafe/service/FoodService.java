package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.food.*;
import com.server.esgcafe.domain.entity.*;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.FoodRepository;
import com.server.esgcafe.repository.UserBreadRepository;
import com.server.esgcafe.repository.UserRepository;
import com.server.esgcafe.repository.UserRewardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    private final UserRewardRepository userRewardRepository;
    private final UserBreadRepository userBreadRepository;

    public FoodCheckResponse checkUserCanMakeFood(FoodCheckRequest request) {

        log.info("🍞checkUserCanMakeFood 시작 - 닉네임: {}, 음식 이름: {}", request.getNickname(), request.getFoodName());

        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Food food = foodRepository.findByName(request.getFoodName())
                .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

        List<UserReward> userRewards = userRewardRepository.findByUser(user);
        List<FoodIngredientDTO> foodIngredientDTOs = new ArrayList<>();

        // 음식의 모든 레시피를 확인
        for (FoodRecipe recipe : food.getRecipes()) {
                // RecipeIngredient에서 ingredient와 quantity 정보 확인
                Ingredient ingredient = recipe.getIngredient(); // Ingredient 객체 가져오기
                int requiredQuantity = recipe.getQuantity(); // 필요한 수량
                String ingredientName = ingredient.getName(); // 재료 이름

                UserReward userReward = userRewards.stream()
                        .filter(reward -> reward.getRewardName().equals(ingredientName))
                        .findFirst()
                        .orElse(null);

                log.info("🍞 검사 중인 재료 - 이름: {}, 필요한 개수: {}, 유저가 가진 개수: {}",
                        ingredientName, requiredQuantity,
                        userReward != null ? userReward.getRewardCount() : 0);

                if (userReward == null || userReward.getRewardCount() < requiredQuantity) {
                    FoodCheckResponse response = FoodCheckResponse.cannotMake("재료가 부족하여 빵을 만들 수 없습니다.");
                    log.info("🍞checkUserCanMakeFood 실패 - 닉네임: {}, 메시지: {}", request.getNickname(), response.getMessage());

                    return response;
                }

                // 필요한 재료 DTO 추가
                foodIngredientDTOs.add(new FoodIngredientDTO(ingredientName, requiredQuantity));
            }

        List<UserRewardDTO> userRewardDTOs = userRewards.stream()
                .map(ur -> new UserRewardDTO(ur.getRewardName(), ur.getRewardCount()))
                .collect(Collectors.toList());

        FoodCheckResponse response = FoodCheckResponse.canMake("빵을 만들 수 있습니다.", foodIngredientDTOs, userRewardDTOs);
        log.info("🍞checkUserCanMakeFood 성공 - 닉네임: {}, 메시지: {}", request.getNickname(), response.getMessage());
        log.info("🍞필요한 재료: {}", foodIngredientDTOs);
        log.info("🍞유저 보유 재료: {}", userRewardDTOs);

        return response;
    }

    @Transactional
    public FoodUpdateResponse updateUserRewardsAndBread(FoodUpdateRequest request) {

        log.info("🍞 userBread 저장 및 리워드 차감 시작");

        log.info("🍞 request.getnickname: {}", request.getNickname());
        log.info("🍞 request.getFoodName: {}", request.getFoodName());
        log.info("🍞 request.getBreadCount: {}", request.getBreadCount());

        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Food food = foodRepository.findByName(request.getFoodName())
                .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

        // 유저의 보유 빵 개수 업데이트
        UserBread existingUserBread = userBreadRepository.findByUserAndFood(user, food).orElse(null);

        UserBread userBread;

        try {
            if (existingUserBread == null) {
                // 빵이 없는 경우: 새로운 UserBread 객체 생성
                userBread = request.toEntity(user, food, 0);
            } else {
                // 빵이 있는 경우: 개수만 업데이트
                userBread = UserBread.update(existingUserBread, request.getBreadCount());
            }

            userBreadRepository.save(userBread);
        } catch (Exception e) {
            // 빵 저장 중 오류 발생 시, 리워드 차감도 롤백되도록 설정
            throw new RuntimeException("🍞Failed to save bread", e);
        }

        for(RemainingIngredient ingredient : request.getRemainingIngredients()) {

            String ingredientName = ingredient.getIngredientName();
            int remainingQuantity = ingredient.getRemainingQuantity();

            log.info("🍞 차감된 재료 정보 - 이름 : {}, 남은 개수 : {}", ingredientName, remainingQuantity);

            UserReward userReward = userRewardRepository.findByUserAndRewardName(user, ingredientName)
                    .orElseThrow(() -> new AppException(ErrorCode.REWARD_NOT_FOUND));

            // 새로운 UserReward 객체 생성
            UserReward updatedUserReward = userReward.withUpdatedRewardCount(remainingQuantity);
            userRewardRepository.save(updatedUserReward);

        }

        return new FoodUpdateResponse(userBread);
    }
}
