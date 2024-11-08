package com.server.esgcafe.domain.entity;

import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userInventoryNo;

    @ManyToOne
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_no", nullable = false)
    private Food food;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_no", nullable = false)
    private Ingredient ingredient;

    private int count;

    @PrePersist
    public void validate() {
        if ((food == null && ingredient == null) || (food != null && ingredient != null)) {
            throw new AppException(ErrorCode.INVALID_INVENTORY_ITEM);
        }
    }
}
