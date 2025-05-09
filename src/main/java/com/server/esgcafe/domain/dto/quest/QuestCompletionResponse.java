package com.server.esgcafe.domain.dto.quest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestCompletionResponse {

    private Long questCode;
    private int completedCount;

}
