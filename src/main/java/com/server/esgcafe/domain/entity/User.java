package com.server.esgcafe.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;

    private String googleId;
    private String steamId;

    @Column(name = "login_time")
    private Instant loginTime;

    @Column(name = "logout_time")
    private Instant logoutTime;

    private String nickName;

    private String image;

}
