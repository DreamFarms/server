package com.server.esgcafe.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSimpleInfoResponse {

    private String nickName;
    private long cash;
    private long gold;
}
