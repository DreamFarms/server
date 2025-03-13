package com.server.esgcafe.controller;

import com.server.esgcafe.domain.dto.game.BreadSaleRequestDTO;
import com.server.esgcafe.domain.dto.game.GameResultRequest;
import com.server.esgcafe.domain.dto.game.GameResultResponse;
import com.server.esgcafe.exception.Response;
import com.server.esgcafe.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/result")
    public Response<GameResultResponse> resultSaveGame(@RequestBody GameResultRequest request) {

        GameResultResponse response = gameService.saveGameResult(request);
        return Response.success(response);
    }

    @PostMapping("/sell")
    public Response<String> sellBread(@RequestBody BreadSaleRequestDTO request) {
        gameService.sellBreadAsync(request);
        return Response.success("빵 판매 요청 완료. (비동기 처리 중)");
    }
}
