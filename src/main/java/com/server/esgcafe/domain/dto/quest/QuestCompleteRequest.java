package com.server.esgcafe.domain.dto.quest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestCompleteRequest {

    private String nickname;
    private Long questCode;

}
