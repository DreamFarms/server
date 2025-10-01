package com.server.esgcafe.controller;

import com.server.esgcafe.domain.dto.npc.NpcCollectionBook;
import com.server.esgcafe.exception.Response;
import com.server.esgcafe.service.NpcCollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/npc")
@RequiredArgsConstructor
public class NpcCollectionController {

    private final NpcCollectionService npcCollectionService;

    @GetMapping("/collection")
    public Response<List<NpcCollectionBook>> getCollection(@RequestParam String nickname) {
        return Response.success(npcCollectionService.getCollectionByNickname(nickname));
    }

}
