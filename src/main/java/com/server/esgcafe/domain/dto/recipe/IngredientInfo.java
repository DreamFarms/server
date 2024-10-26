package com.server.esgcafe.domain.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IngredientInfo {

    private String ingredientName;
    private int count;

}
