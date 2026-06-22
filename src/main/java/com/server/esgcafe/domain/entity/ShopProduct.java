package com.server.esgcafe.domain.entity;

import com.server.esgcafe.domain.enum_class.PaymentCurrency;
import com.server.esgcafe.domain.enum_class.ShopProductType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShopProduct extends BaseEntity {
    // 상점에 표시되는 상품

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopProductNo;

    private String productCode;
    private String googleProductId;
    private String productName;

    @Enumerated(EnumType.STRING)
    private ShopProductType productType;

    @Enumerated(EnumType.STRING)
    private PaymentCurrency paymentCurrency;

    private Integer priceAmount;

    private boolean consumable;
    private boolean active;

    public boolean isRealMoneyProduct() {
        return this.paymentCurrency == PaymentCurrency.REAL_MONEY;
    }

    public boolean isCurrencyProduct() {
        return this.paymentCurrency == PaymentCurrency.GOLD
                || this.paymentCurrency == PaymentCurrency.CASH;
    }

    public boolean isCashChargeProduct() {
        return this.productType == ShopProductType.CASH_CHARGE;
    }

    public boolean isGoldPackageProduct() {
        return this.productType == ShopProductType.GOLD_PACKAGE;
    }

    public boolean isItemPackageProduct() {
        return this.productType == ShopProductType.ITEM_PACKAGE;
    }

}
