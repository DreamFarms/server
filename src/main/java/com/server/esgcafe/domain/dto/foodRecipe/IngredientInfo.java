package com.server.esgcafe.domain.dto.foodRecipe;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IngredientInfo {

    private String ingredientName;
    private int count;

}
