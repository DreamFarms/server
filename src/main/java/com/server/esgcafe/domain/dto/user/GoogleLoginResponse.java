package com.server.esgcafe.domain.dto.user;

import com.server.esgcafe.domain.dto.foodRecipe.UnlockedRecipeInfo;
import com.server.esgcafe.domain.dto.userInventory.UserInventoryResponse;
import com.server.esgcafe.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleLoginResponse {

    private String status;
    private String message;
    private String accessToken;
    private String refreshToken;
    private Long userNo;
    private String nickName;
    private long gold;
    private long cash;

    private List<UserInventoryResponse> inventory;
    private List<UnlockedRecipeInfo> unlockedRecipes;

    public static GoogleLoginResponse success(
            TokenDto tokenDto,
            User user,
            List<UserInventoryResponse> inventory,
            List<UnlockedRecipeInfo> unlockedRecipes
    ) {
        return GoogleLoginResponse.builder()
                .status("success")
                .message("ID Token is valid.")
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .userNo(user.getUserNo())
                .nickName(user.getNickName())
                .gold(user.getGold())
                .cash(user.getCash())
                .inventory(inventory)
                .unlockedRecipes(unlockedRecipes)
                .build();
    }

    public static GoogleLoginResponse error(String errorMessage) {
        return GoogleLoginResponse.builder()
                .status("error")
                .message(errorMessage)
                .accessToken(null)
                .refreshToken(null)
                .userNo(null)
                .nickName(null)
                .accessToken(null)
                .refreshToken(null)
                .inventory(Collections.emptyList())
                .unlockedRecipes(Collections.emptyList())
                .build();
    }
}