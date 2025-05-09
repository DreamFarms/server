package com.server.esgcafe.domain.dto.quest;

import com.server.esgcafe.domain.entity.Quest;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserQuestProgress;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserQuestProgressDTO {

    private int completedCount;
    private LocalDateTime lastCompletedAt;
    private Quest quest;

    public UserQuestProgress toEntity(User user) {
        return new UserQuestProgress(
                user,
                this.quest,
                this.completedCount,
                this.lastCompletedAt
        );
    }

}
