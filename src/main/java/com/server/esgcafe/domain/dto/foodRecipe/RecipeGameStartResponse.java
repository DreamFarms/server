package com.server.esgcafe.domain.dto.foodRecipe;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RecipeGameStartResponse {

    private List<IngredientInfo> ingredients;
    private List<UnlockedRecipeInfo> unlockedRecipes;

}
