package com.server.esgcafe.controller;

import com.server.esgcafe.domain.dto.recipe.RecipeGameStartResponse;
import com.server.esgcafe.exception.Response;
import com.server.esgcafe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    // 레시피 찾기 게임 시작 요청
    @GetMapping("/start")
    public Response<RecipeGameStartResponse> startRecipeGame(@RequestParam String nickname) {

        RecipeGameStartResponse response = recipeService.startRecipeGame(nickname);
        return Response.success(response);

    }

}
