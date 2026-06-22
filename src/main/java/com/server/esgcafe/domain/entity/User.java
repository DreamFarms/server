package com.server.esgcafe.domain.entity;

import com.server.esgcafe.domain.enum_class.CurrencyType;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;

    private String googleId;
    private String email;

    @Column(name = "login_time")
    private Instant loginTime;

    @Column(name = "logout_time")
    private Instant logoutTime;

    private String nickName;

    private String refreshToken;

    @Column(name = "gold")
    private long gold;  // 유저가 보유한 게임 재화

    @Column(name = "cash")
    private long cash;  // 결제로 추가하는 재화

    @Column(name = "ticket")
    private int ticket;  // 유저가 보유한 티켓 수

    // 닉네임 추가
    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    // 로그인 시간 갱신
    public void updateLoginTime(Instant loginTime) {
        this.loginTime = loginTime;
    }

    // Refresh Token 업데이트
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // 골드 추가
    public void addGold(long amount) {
        this.gold += amount;
    }

    public void useGold(long amount) {
        validatePositiveAmount(amount);

        if (this.gold < amount) {
            throw new AppException(ErrorCode.INSUFFICIENT_GOLD);
        }

        this.gold -= amount;
    }

    public void addCash(long amount) {
        validatePositiveAmount(amount);
        this.cash += amount;
    }

    public void useCash(long amount) {
        validatePositiveAmount(amount);

        if (this.cash < amount) {
            throw new AppException(ErrorCode.INSUFFICIENT_CASH);
        }

        this.cash -= amount;
    }

    public void addTicket(int amount) {
        this.ticket += amount;
    }

    public void useTicket(int amount) {
        if (this.ticket < amount) {
            throw new AppException(ErrorCode.NOT_ENOUGH_TICKET);
        }

        this.ticket -= amount;
    }

    public long getCurrencyBalance(CurrencyType currencyType) {
        if (currencyType == CurrencyType.GOLD) {
            return this.gold;
        }

        if (currencyType == CurrencyType.CASH) {
            return this.cash;
        }

        throw new AppException(ErrorCode.INVALID_REQUEST);
    }

    private void validatePositiveAmount(long amount) {
        if (amount <= 0) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
    }
}