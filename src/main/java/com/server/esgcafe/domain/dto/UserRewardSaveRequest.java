package com.server.esgcafe.domain.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRewardSaveRequest {

    private List<UserRewardInfo> rewards;

}
