package com.server.esgcafe.domain.dto.user;

import com.server.esgcafe.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleUserInfo {

    private String googleId;
    private String email;
    private String nickName;

    // DTO를 Entity로 변환하는 메서드
    public User toEntity() {
        return User.builder()
                .googleId(googleId)
                .email(email)
                .nickName(nickName)
                .loginTime(Instant.now()) // 기본값 설정
                .build();
    }

    public static GoogleUserInfo from (String googleId, String email, String nickName) {
        return GoogleUserInfo.builder()
                .googleId(googleId)
                .email(email)
                .nickName(nickName)
                .build();
    }

}
