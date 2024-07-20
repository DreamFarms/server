package com.server.esgcafe.domain.dto;

import com.server.esgcafe.domain.entity.UserReward;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRewardSaveResponse {

    private boolean isSaved;
    private LocalDateTime saveTime;

    public UserRewardSaveResponse(UserReward userReward) {
        this.isSaved = true;
        this.saveTime = userReward.getCreatedAt();
    }

}
