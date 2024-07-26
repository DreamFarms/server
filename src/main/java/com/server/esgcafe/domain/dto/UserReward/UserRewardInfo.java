package com.server.esgcafe.domain.dto.UserReward;

import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserReward;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRewardInfo {

    private String name;
    private int count;

    public UserReward toEntity(User user) {
        return UserReward.builder()
                .user(user)
                .rewardName(this.name)
                .rewardCount(this.count)
                .build();
    }
}
