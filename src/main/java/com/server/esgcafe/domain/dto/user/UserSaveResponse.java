package com.server.esgcafe.domain.dto.user;

import com.server.esgcafe.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSaveResponse {

    private String nickname;
    private String message;

    public static UserSaveResponse from(User user, String message) {
        return new UserSaveResponse(user.getNickName(), message);
    }
}
