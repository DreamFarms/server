package com.server.esgcafe.domain.dto.foodRecipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRecipeGuessRequest {

    private String nickname;
    private List<IngredientQuantity> ingredients;

}
