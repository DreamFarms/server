package com.server.esgcafe.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FoodIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodIngredientNo;

    @JoinColumn(name = "food_no")
    @ManyToOne(fetch = FetchType.LAZY)
    private Food food;

    @JoinColumn(name = "ingredient_no")
    @ManyToOne(fetch = FetchType.LAZY)
    private Ingredient ingredient;

    @Column(nullable = false)
    private int quantity;  // 해당 음식을 만들기 위해 필요한 재료 수량
}
