package com.server.esgcafe.domain.entity;

import com.server.esgcafe.domain.enum_class.PaymentCurrency;
import com.server.esgcafe.domain.enum_class.PurchaseStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PurchaseTransaction extends BaseEntity{
    // 구매 1건의 기록

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseTransactionNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_no", nullable = false)
    private ShopProduct shopProduct;

    private String productCode;

    private String googleProductId;

    private String purchaseToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentCurrency paymentCurrency;

    private int priceAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PurchaseStatus purchaseStatus;

    private String googlePurchaseState;

    private String googleOrderId;

    private boolean acknowledged;

    private boolean consumed;

    private boolean rewardGranted;

    private Instant requestedAt;

    private Instant verifiedAt;

    private Instant rewardGrantedAt;

    private Instant acknowledgedAt;

    private Instant consumedAt;

    @PrePersist
    protected void onCreate() {
        this.requestedAt = Instant.now();
    }

    public void markPending(String googlePurchaseState) {
        this.purchaseStatus = PurchaseStatus.PENDING;
        this.googlePurchaseState = googlePurchaseState;
        this.verifiedAt = Instant.now();
    }

    public void markPurchased(String googlePurchaseState, String googleOrderId) {
        this.purchaseStatus = PurchaseStatus.PURCHASED;
        this.googlePurchaseState = googlePurchaseState;
        this.googleOrderId = googleOrderId;
        this.verifiedAt = Instant.now();
    }

    public void markVerifyFailed(String googlePurchaseState) {
        this.purchaseStatus = PurchaseStatus.VERIFY_FAILED;
        this.googlePurchaseState = googlePurchaseState;
        this.verifiedAt = Instant.now();
    }

    public void markRewardGranted() {
        this.purchaseStatus = PurchaseStatus.REWARD_GRANTED;
        this.rewardGranted = true;
        this.rewardGrantedAt = Instant.now();
    }

    public void markAlreadyProcessed() {
        this.purchaseStatus = PurchaseStatus.ALREADY_PROCESSED;
    }

    public void markAcknowledged() {
        this.purchaseStatus = PurchaseStatus.ACKNOWLEDGED;
        this.acknowledged = true;
        this.acknowledgedAt = Instant.now();
    }

    public void markConsumed() {
        this.purchaseStatus = PurchaseStatus.CONSUMED;
        this.consumed = true;
        this.consumedAt = Instant.now();
    }

    public void markCanceled() {
        this.purchaseStatus = PurchaseStatus.CANCELED;
    }

    public void markRefunded() {
        this.purchaseStatus = PurchaseStatus.REFUNDED;
    }

    public void markFailed() {
        this.purchaseStatus = PurchaseStatus.FAILED;
    }
}