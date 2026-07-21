package com.server.esgcafe.domain.dto.user;

import com.server.esgcafe.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSaveTestResponse {

    private Long userNo;

    private String nickname;

    private long gold;

    private long cash;

    private int ticket;

    private String message;

    public static UserSaveTestResponse from(User user, String message) {
        return new UserSaveTestResponse(
                user.getUserNo(),
                user.getNickName(),
                user.getGold(),
                user.getCash(),
                user.getTicket(),
                message
        );
    }
}
