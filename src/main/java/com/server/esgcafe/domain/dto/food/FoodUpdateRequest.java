package com.server.esgcafe.domain.dto.food;

import com.server.esgcafe.domain.entity.Food;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserBread;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodUpdateRequest {

    private String nickname;
    private String foodName;
    private List<RemainingIngredient> remainingIngredients;
    private int breadCount;

    public UserBread toEntity(User user, Food food, int currentBreadCount) {
        return UserBread.builder()
                .user(user)
                .food(food)
                .breadCount(breadCount)
                .build();
    }

}
