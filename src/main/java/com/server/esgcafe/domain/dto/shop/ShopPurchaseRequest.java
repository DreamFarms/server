package com.server.esgcafe.domain.dto.shop;

import com.server.esgcafe.domain.entity.PurchaseTransaction;
import com.server.esgcafe.domain.entity.ShopProduct;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.enum_class.PurchaseStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShopPurchaseRequest {

    private String nickname;
    private String productCode;

    public PurchaseTransaction toTransactionEntity(User user, ShopProduct product) {
        return PurchaseTransaction.builder()
                .user(user)
                .shopProduct(product)
                .productCode(product.getProductCode())
                .googleProductId(product.getGoogleProductId())
                .paymentCurrency(product.getPaymentCurrency())
                .priceAmount(product.getPriceAmount())
                .purchaseStatus(PurchaseStatus.REQUESTED)
                .rewardGranted(false)
                .acknowledged(false)
                .consumed(false)
                .build();
    }
}