package com.server.esgcafe.service;

import com.server.esgcafe.domain.entity.Food;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserUnlockedRecipe;
import com.server.esgcafe.repository.UserUnlockedRecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUnlockedRecipeService {

    private final UserUnlockedRecipeRepository userUnlockedRecipeRepository;

    public void saveUnlockedRecipe(User user, Food food) {
        // 이미 해금된 레시피인지 확인
        boolean isAlreadyUnlocked = userUnlockedRecipeRepository
                .existsByUserAndFood(user, food);

        if (!isAlreadyUnlocked) {
            UserUnlockedRecipe unlockedRecipe = UserUnlockedRecipe.builder()
                    .user(user)
                    .food(food)
                    .build();
            userUnlockedRecipeRepository.save(unlockedRecipe);
        }
    }
}