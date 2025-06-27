package com.server.esgcafe.domain.entity;

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

}
