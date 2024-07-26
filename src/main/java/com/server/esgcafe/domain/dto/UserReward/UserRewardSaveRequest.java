package com.server.esgcafe.domain.dto.UserReward;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRewardSaveRequest {

    private String nickname;
    private List<UserRewardInfo> rewards;

}
