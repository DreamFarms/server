package com.server.esgcafe.domain.dto.foodRecipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeGuessResponse {

    private boolean result; // 성공 여부
    private String breadName; // 빵 이름(성공 시)
}
