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

    // 로그인 시간 갱신
    public void updateLoginTime(Instant loginTime) {
        this.loginTime = loginTime;
    }

    // Refresh Token 업데이트
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
