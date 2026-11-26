package com.server.esgcafe.domain.dto.kneading;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KneadingStartResponse {
    private Long attemptNo;
    private String foodName;
}
