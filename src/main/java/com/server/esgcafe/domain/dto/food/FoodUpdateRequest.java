package com.server.esgcafe.domain.dto.food;

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

}
