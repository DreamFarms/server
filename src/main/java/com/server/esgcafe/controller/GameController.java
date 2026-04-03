package com.server.esgcafe.controller;

import com.server.esgcafe.domain.dto.game.*;
import com.server.esgcafe.exception.Response;
import com.server.esgcafe.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/sell")
    public Response<SellBreadResponse> sellBread(@RequestBody BreadSaleRequest request) {
//        gameService.sellBreadAsync(request);
        SellBreadResponse response = gameService.sellBreadNow(request);
        return Response.success(response);
    }

    @PostMapping("/enter")
    public Response<StoreEnterResponse> enterStore(@RequestBody StoreEnterRequest request) {
        StoreEnterResponse response = gameService.enterStore(request);
        return Response.success(response);
    }
}
