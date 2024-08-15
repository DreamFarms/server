package com.server.esgcafe.domain.dto.food;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodCheckResponse {

    private String message;
    private List<FoodIngredientDTO> foodIngredients;
    private List<UserRewardDTO> userRewards;


    public static FoodCheckResponse cannotMake(String message) {
        return new FoodCheckResponse(message, null, null);
    }

    public static FoodCheckResponse canMake(String message, List<FoodIngredientDTO> foodIngredients, List<UserRewardDTO> userRewards) {
        return new FoodCheckResponse(message, foodIngredients, userRewards);
    }




}

