package com.server.esgcafe.controller;

import com.server.esgcafe.domain.dto.food.FoodCheckRequest;
import com.server.esgcafe.domain.dto.food.FoodCheckResponse;
import com.server.esgcafe.exception.Response;
import com.server.esgcafe.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @PostMapping("/check")
    public Response<FoodCheckResponse> check(@RequestBody FoodCheckRequest request) {

        FoodCheckResponse response = foodService.checkUserCanMakeFood(request);
        return Response.success(response);
    }

}
