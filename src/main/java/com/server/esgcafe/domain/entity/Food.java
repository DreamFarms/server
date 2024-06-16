package com.server.esgcafe.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodNo;

    private String name;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL)
    private List<FoodIngredient> foodIngredients; // 음식을 만들기 위해 필요한 재료 목록

}
