package com.server.esgcafe.domain.dto.shop;

import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserInventory;
import com.server.esgcafe.domain.enum_class.ItemType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShopInventoryRewardInfo {
    // 상점 보상으로 받은 재료를 UserInventory Entity로 변환

    private Long foodOrIngredientNo;
    private ItemType itemType;
    private int count;

    public UserInventory toEntity(User user) {
        return UserInventory.builder()
                .user(user)
                .foodOrIngredientNo(foodOrIngredientNo)
                .itemType(itemType)
                .count(count)
                .build();
    }
}

