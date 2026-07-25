package com.server.esgcafe.domain.dto.userInventory;

import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserInventory;
import com.server.esgcafe.domain.enum_class.ItemType;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserInventoryInfo {

    private String name;
    private int count;

    public UserInventory toEntity(User user, Long foodOrIngredientNo, ItemType itemType) {
        return UserInventory.builder()
                .user(user)
                .foodOrIngredientNo(foodOrIngredientNo)
                .itemType(itemType)
                .count(this.count)
                .build();
    }
}