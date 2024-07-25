package com.server.esgcafe.domain.dto.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
    private String nickname;

    public static UserResponse from(String nickname) {
        return new UserResponse(nickname);
    }
}