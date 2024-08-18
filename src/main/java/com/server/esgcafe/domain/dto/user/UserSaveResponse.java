package com.server.esgcafe.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSaveResponse {
    private String nickname;

    public static UserSaveResponse from(String nickname) {
        return new UserSaveResponse(nickname);
    }
}