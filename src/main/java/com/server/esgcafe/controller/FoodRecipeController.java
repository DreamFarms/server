package com.server.esgcafe.controller;

import com.server.esgcafe.domain.dto.foodRecipe.RecipeGameStartResponse;
import com.server.esgcafe.domain.dto.foodRecipe.RecipeGuessResponse;
import com.server.esgcafe.domain.dto.foodRecipe.UserRecipeGuessRequest;
import com.server.esgcafe.exception.Response;
import com.server.esgcafe.service.FoodRecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recipes")
@RequiredArgsConstructor
public class FoodRecipeController {

    private final FoodRecipeService recipeService;

    // 레시피 찾기 게임 시작 요청
    @GetMapping("/start")
    public Response<RecipeGameStartResponse> startRecipeGame(@RequestParam String nickname) {

        RecipeGameStartResponse response = recipeService.startRecipeGame(nickname);
        return Response.success(response);

    }

    // 레시피 찾기 결과 전송
    @PostMapping("/check")
    public Response<RecipeGuessResponse> checkRecipeGame(@RequestBody UserRecipeGuessRequest request) {

        RecipeGuessResponse response = recipeService.checkUserRecipe(request);
        return Response.success(response);
    }

}
