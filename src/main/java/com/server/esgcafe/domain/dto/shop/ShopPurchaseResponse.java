package com.server.esgcafe.domain.dto.shop;

import com.server.esgcafe.domain.entity.PurchaseTransaction;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.enum_class.PaymentCurrency;
import com.server.esgcafe.domain.enum_class.PurchaseStatus;
import com.server.esgcafe.domain.enum_class.ShopRewardType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ShopPurchaseResponse {

    private Long purchaseTransactionNo;

    private String productCode;

    private PaymentCurrency paymentCurrency;

    private int priceAmount;

    private PurchaseStatus purchaseStatus;

    private long gold;

    private long cash;

    private int ticket;

    private List<ShopProductRewardResponse> rewards;

    public static ShopPurchaseResponse of(
            PurchaseTransaction transaction,
            User user, List<ShopProductRewardResponse> rewards
    ) {
        return ShopPurchaseResponse.builder()
                .purchaseTransactionNo(transaction.getPurchaseTransactionNo())
                .productCode(transaction.getProductCode())
                .paymentCurrency(transaction.getPaymentCurrency())
                .priceAmount(transaction.getPriceAmount())
                .purchaseStatus(transaction.getPurchaseStatus())
                .gold(user.getGold())
                .cash(user.getCash())
                .ticket(user.getTicket())
                .rewards(rewards)
                .build();
    }
}