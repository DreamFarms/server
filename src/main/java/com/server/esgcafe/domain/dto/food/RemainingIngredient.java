package com.server.esgcafe.domain.dto.food;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemainingIngredient {

    private String ingredientName;
    private int remainingQuantity;

}
