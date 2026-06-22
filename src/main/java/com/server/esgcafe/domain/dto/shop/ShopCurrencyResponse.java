package com.server.esgcafe.domain.dto.shop;

import com.server.esgcafe.domain.entity.User;
import lombok.*;
@Getter
@Builder
public class ShopCurrencyResponse {

    private long gold;
    private long cash;

    public static ShopCurrencyResponse from(User user) {
        return ShopCurrencyResponse.builder()
                .gold(user.getGold())
                .cash(user.getCash())
                .build();
    }
}
