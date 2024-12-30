package com.server.esgcafe.domain.dto.foodRecipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeGuessResponse {

    private int resultState; // 성공 여부
    private String breadName; // 빵 이름(성공 시)
    private String category;
}
