package com.server.esgcafe.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.server.esgcafe.configuration.jwt.JwtProvider;
import com.server.esgcafe.domain.dto.foodRecipe.UnlockedRecipeInfo;
import com.server.esgcafe.domain.dto.user.GoogleLoginRequest;
import com.server.esgcafe.domain.dto.user.GoogleLoginResponse;
import com.server.esgcafe.domain.dto.user.TokenDto;
import com.server.esgcafe.domain.dto.userInventory.UserInventoryResponse;
import com.server.esgcafe.domain.entity.*;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GoogleLoginService {

    @Value("${google.client-id}")
    String CLIENT_ID;

    @Value("${jwt.secret.key}")
    private String salt;

    private Key secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }

    private final GoogleIdTokenVerifier verifier;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final IngredientRepository ingredientRepository;
    private final UserInventoryRepository userInventoryRepository;
    private final UserUnlockedRecipeRepository userUnlockedRecipeRepository;


    public GoogleLoginService(@Value("${google.client-id}") String clientId, UserRepository userRepository,
                              JwtProvider jwtProvider, UserInventoryRepository userInventoryRepository,
                              UserUnlockedRecipeRepository userUnlockedRecipeRepository, FoodRepository foodRepository, IngredientRepository ingredientRepository) {

        if (clientId == null || clientId.isEmpty()) {
            throw new AppException(ErrorCode.MISSING_GOOGLE_CLIENT_ID);
        }
        System.out.println("Client ID: " + clientId);

        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.foodRepository = foodRepository;
        this.ingredientRepository = ingredientRepository;
        this.userInventoryRepository = userInventoryRepository;
        this.userUnlockedRecipeRepository = userUnlockedRecipeRepository;

        this.verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory()
        )
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    // ID Token을 받아 검증하고 검증 결과와 JWT 반환
    public GoogleLoginResponse processLoginToken(GoogleLoginRequest request) {

        try {
            String idToken = request.getIdToken();
            log.info("idToken : {}", idToken);

            GoogleIdToken.Payload payload = verifyIdToken(idToken);

            String googleId = payload.getSubject();
            String email = payload.getEmail();

            User user = userRepository.findByGoogleId(googleId)
                    .orElseGet(() -> {
                        User newUser = User.builder()
                                .googleId(googleId)
                                .email(email)
                                .build();
                        userRepository.save(newUser);
                        return newUser;
                    });

            TokenDto tokenDto = jwtProvider.createToken(googleId, email);
            user.updateRefreshToken(tokenDto.getRefreshToken());
            userRepository.save(user);

            // 1. 인벤토리 + 이름 조회
            List<UserInventory> inventoryEntities = userInventoryRepository.findByUser(user);
            List<UserInventoryResponse> inventoryList = inventoryEntities.stream()
                    .map(inv -> {
                        String name = switch (inv.getItemType()) {
                            case FOOD -> foodRepository.findById(inv.getFoodOrIngredientNo())
                                    .map(Food::getName)
                                    .orElse("Unknown Food");
                            case INGREDIENT -> ingredientRepository.findById(inv.getFoodOrIngredientNo())
                                    .map(Ingredient::getName)
                                    .orElse("Unknown Ingredient");
                        };

                        return new UserInventoryResponse(
                                inv.getFoodOrIngredientNo().intValue(),
                                name,
                                inv.getCount()
                        );
                    })
                    .collect(Collectors.toList());

            // 2. 해금 레시피 변환
            List<UserUnlockedRecipe> recipeEntities = userUnlockedRecipeRepository.findByUserWithFood(user);
            List<UnlockedRecipeInfo> recipeList = recipeEntities.stream()
                    .map(ur -> new UnlockedRecipeInfo(
                            ur.getFood().getName(),
                            ur.getFood().getCategory()
                    ))
                    .collect(Collectors.toList());

            // 3. 최종 응답
            return GoogleLoginResponse.success(tokenDto, inventoryList, recipeList);

        } catch (Exception e) {
            log.error("Login error", e);
            return GoogleLoginResponse.error(e.getMessage());
        }
    }

    // Refresh Token을 사용한 Access Token 재발급
    public String refreshAccessToken(String refreshToken) {
        try {
            // Refresh Token 복호화 및 검증
            String encryptedRefreshToken = jwtProvider.validateAndDecryptRefreshToken(refreshToken);

            // Refresh Token에서 userId 추출
            Claims claims = jwtProvider.parseClaims(encryptedRefreshToken);
            String userId = claims.getSubject();

            // 새로운 Access Token 발급
            return jwtProvider.reIssueAccessToken(encryptedRefreshToken);

        } catch (Exception e) {
            log.error("Refresh Access Token error: {}", e.getMessage());
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN, e.getMessage());
        }
    }

    private GoogleIdToken.Payload verifyIdToken(String idToken) throws GeneralSecurityException, IOException {
        GoogleIdToken token = verifier.verify(idToken);
        if (token == null) {
            throw new IllegalArgumentException("Invalid ID Token.");
        }
        return token.getPayload();
    }

}