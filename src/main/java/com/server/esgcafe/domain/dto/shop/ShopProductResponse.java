package com.server.esgcafe.domain.dto.shop;

import com.server.esgcafe.domain.entity.ShopProduct;
import com.server.esgcafe.domain.enum_class.PaymentCurrency;
import com.server.esgcafe.domain.enum_class.ShopProductType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ShopProductResponse {

    private Long shopProductNo;
    private String productCode;
    private String googleProductId;
    private String productName;
    private ShopProductType productType;
    private PaymentCurrency paymentCurrency;
    private int priceAmount;
    private boolean consumable;

    private List<ShopProductRewardResponse> rewards;

    public static ShopProductResponse from(ShopProduct product, List<ShopProductRewardResponse> rewards) {
        return ShopProductResponse.builder()
                .shopProductNo(product.getShopProductNo())
                .productCode(product.getProductCode())
                .googleProductId(product.getGoogleProductId())
                .productName(product.getProductName())
                .productType(product.getProductType())
                .paymentCurrency(product.getPaymentCurrency())
                .priceAmount(product.getPriceAmount())
                .consumable(product.isConsumable())
                .rewards(rewards)
                .build();

    }

}
