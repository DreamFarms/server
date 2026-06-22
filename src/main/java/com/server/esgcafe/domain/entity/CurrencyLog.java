package com.server.esgcafe.domain.entity;

import com.server.esgcafe.domain.enum_class.CurrencyChangeReason;
import com.server.esgcafe.domain.enum_class.CurrencyType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CurrencyLog extends BaseEntity{
    // user의 gold와 cash가 왜 늘고 줄었는지 기록

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long currencyLogNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_no")
    private PurchaseTransaction purchaseTransaction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CurrencyType currencyType;

    private long changeAmount;
    private long balanceAfter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CurrencyChangeReason reason;

}
