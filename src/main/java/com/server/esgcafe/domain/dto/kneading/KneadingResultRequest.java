package com.server.esgcafe.domain.dto.kneading;

import lombok.Data;

@Data
public class KneadingResultRequest {
    private String nickname;
    private Long attemptNo;
    private String grade;
}
