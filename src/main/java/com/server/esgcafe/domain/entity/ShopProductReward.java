package com.server.esgcafe.domain.entity;

import com.server.esgcafe.domain.enum_class.ShopRewardType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShopProductReward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopProductRewardNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_product_no")
    private ShopProduct shopProduct;

    @Enumerated(EnumType.STRING)
    private ShopRewardType rewardType;

    private String rewardCode;

    private int rewardAmount;
}
