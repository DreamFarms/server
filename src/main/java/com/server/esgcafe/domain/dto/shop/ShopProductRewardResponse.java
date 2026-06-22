package com.server.esgcafe.domain.dto.shop;

import com.server.esgcafe.domain.entity.ShopProductReward;
import com.server.esgcafe.domain.enum_class.ShopRewardType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShopProductRewardResponse {

    private ShopRewardType rewardType;
    private String rewardCode;
    private int rewardAmount;

    public static ShopProductRewardResponse from(ShopProductReward reward) {
        return ShopProductRewardResponse.builder()
                .rewardType(reward.getRewardType())
                .rewardCode(reward.getRewardCode())
                .rewardAmount(reward.getRewardAmount())
                .build();
    }
}