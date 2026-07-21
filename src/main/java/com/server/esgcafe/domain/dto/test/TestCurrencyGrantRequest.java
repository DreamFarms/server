package com.server.esgcafe.domain.dto.test;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TestCurrencyGrantRequest {

    private String nickname;

    private long goldAmount;

    private long cashAmount;
}