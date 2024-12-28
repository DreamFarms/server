package com.server.esgcafe.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.server.esgcafe.domain.dto.user.GoogleLoginRequest;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    public GoogleLoginService(@Value("${google.client-id}") String clientId) {

        if (clientId == null || clientId.isEmpty()) {
            throw new IllegalArgumentException("Google Client ID is required");
        }
        System.out.println("Client ID: " + clientId);

        this.verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory()
        )
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    // ID Token을 받아 검증하고 검증 결과와 JWT 반환
    public Map<String, String> processLoginToken(GoogleLoginRequest request) {

        String idToken = request.getIdToken();

        log.info("🎟idToken : {} ", idToken);

        Map<String, String> response = new HashMap<>();

        try {
            // ID Token 검증
            GoogleIdToken.Payload idPayload = verifier(idToken);
            if (idPayload == null) {
                response.put("status", "fail");
                response.put("message", "Invalid ID Token");
                return response;
            }

            // 검증 성공 시 JWT 발급
            String jwt = createJwtToken(idPayload);
            response.put("status", "success");
            response.put("message", "ID Token is valid.");
            response.put("jwt", jwt);
            return response;

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "An error occurred: " + e.getMessage());
            return response;
        }
    }

    // ID Token 검증
    private GoogleIdToken.Payload verifier(String idToken) throws GeneralSecurityException, IOException {

        log.info("🎟idToken : {} ", idToken);

        if (idToken == null || idToken.isEmpty()) {
            throw new AppException(ErrorCode.MALFORMED_ID_TOKEN);
        }

        // 먼저 디코딩하여 payload의 audience(aud)를 확인
        GoogleIdToken token = GoogleIdToken.parse(new GsonFactory(), idToken);  // GoogleIdToken 객체를 직접 파싱하여 확인
        if (token == null) {
            throw new IllegalArgumentException("Invalid ID Token format.");
        }

        GoogleIdToken.Payload payload = token.getPayload();

        // 1. Audience(aud) 검증
        // audience 값이 Object 타입으로 반환될 수 있기 때문에 String으로 변환
        Object audienceObj = payload.getAudience();
        String audience = (audienceObj instanceof String) ? (String) audienceObj : null;

        log.info("🎟audience in token : {}", audience);

        // audience가 맞는지 확인 (clientId와 일치하는지)
        if (!CLIENT_ID.equals(audience)) {
            throw new AppException(ErrorCode.INVALID_AUDIENCE);
        }

        // 2. 만료 시간(exp) 확인
        long expirationTimeSeconds = payload.getExpirationTimeSeconds();
        if (expirationTimeSeconds <= System.currentTimeMillis() / 1000) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }

        // 3. 발행자(iss) 확인
        String issuer = payload.getIssuer();
        if (!"https://accounts.google.com".equals(issuer)) {
            throw new AppException(ErrorCode.INVALID_ISSUER);
        }

        // GoogleIdTokenVerifier로 실제 서명 검증
        GoogleIdToken verifiedToken = verifier.verify(idToken);
        if (verifiedToken == null) {
            throw new AppException(ErrorCode.ID_TOKEN_VERIFICATION_FAILED);
        }

        return verifiedToken.getPayload();
    }

    // JWT 생성
    private String createJwtToken(GoogleIdToken.Payload payload) {
        String userId = payload.getSubject();
        String email = payload.getEmail();

        // JWT 생성
        return Jwts.builder()
                .setSubject(userId)
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
