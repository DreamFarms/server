package com.server.esgcafe.controller;

import com.server.esgcafe.domain.dto.quest.QuestCompleteRequest;
import com.server.esgcafe.domain.dto.quest.QuestCompletionResponse;
import com.server.esgcafe.exception.Response;
import com.server.esgcafe.service.QuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/quest")
@RequiredArgsConstructor
public class QuestController {

    private final QuestService questService;

    @PostMapping("/complete")
    public Response<QuestCompletionResponse> completeQuest(QuestCompleteRequest request) {

        QuestCompletionResponse response = questService.saveQuestCompletion(request);
        return Response.success(response);
    }
}
