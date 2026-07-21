package com.server.esgcafe.domain.dto.test;

import com.server.esgcafe.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TestCurrencyGrantResponse {

    private String nickname;
    private long gold;
    private long cash;
    private int ticket;

    public static TestCurrencyGrantResponse from(User user) {
        return TestCurrencyGrantResponse.builder()
                .nickname(user.getNickName())
                .gold(user.getGold())
                .cash(user.getCash())
                .ticket(user.getTicket())
                .build();
    }
}