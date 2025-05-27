package com.server.esgcafe.configuration.jwt;

import com.server.esgcafe.domain.dto.user.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret.key}")
    private String salt;

    @Value("${jwt.refresh.secret}")
    private String refreshKey;

    private Key secretKey;
    private CryptoUtil cryptoUtil;

    // Access Token 만료 = 1시간, Refresh Token 만료 = 7일
//    private final long accessTokenValidTime = 7 * 24 * 60 * 60 * 1000L;   // 60 * 60 * 1000L
    private final long accessTokenValidTime = 30L * 24 * 60 * 60 * 1000; // 테스트용 만료 30일
    private final long refreshTokenValidTime = 7 * 24 * 60 * 60 * 1000L;


    @PostConstruct
    protected void init() {
        try {
            // Access Token용 SecretKey 생성
            secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));

            // 16진수 문자열을 바이트 배열로 변환
            byte[] decodedKey = Hex.decodeHex(refreshKey.toCharArray());
            log.info("🔑 Decoded AES Key Length Before Validation: {}", decodedKey.length); // 길이 확인

            // AES 키 길이 검증 (16, 24, 32바이트로 자르기)
            if (decodedKey.length != 16 && decodedKey.length != 24 && decodedKey.length != 32) {
                // 길이를 맞춰주기 위해 32바이트로 자르거나 변경
                decodedKey = Arrays.copyOf(decodedKey, 32);  // 32바이트로 맞추기
                log.info("⚠️ Adjusted AES Key Length to 32 bytes.");
            }

            // SecretKey 생성
            SecretKey refreshSecretKey = new SecretKeySpec(decodedKey, "AES");

            // CryptoUtil 초기화
            cryptoUtil = new CryptoUtil(refreshSecretKey.getEncoded()); // 수정된 부분
        } catch (IllegalArgumentException e) {
            log.error("❌ Failed to initialize CryptoUtil: {}", e.getMessage());
            throw new RuntimeException("Invalid refresh key", e);
        } catch (Exception e) {
            log.error("❌ Error initializing JwtProvider: {}", e.getMessage());
            throw new RuntimeException("Error initializing JwtProvider", e);
        }
    }

    /**
     * Access Token + Refresh Token 동시 발급
     */
    public TokenDto createToken(String googleId, String email) {

        // Access Token
        long now = System.currentTimeMillis();
        Date accessExpire = new Date(now + accessTokenValidTime);
        String accessToken = Jwts.builder()
                .setSubject(googleId)
                .claim("email", email)
                .setIssuedAt(new Date(now))
                .setExpiration(accessExpire)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token
        Date refreshExpire = new Date(now + refreshTokenValidTime);
        String refreshToken = Jwts.builder()
                .setSubject(googleId)
                .claim("type", "refresh")
                .setIssuedAt(new Date(now))
                .setExpiration(refreshExpire)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 암호화
        String encryptedRefreshToken = cryptoUtil.encrypt(refreshToken);

        return new TokenDto(accessToken, encryptedRefreshToken);
    }

    // Refresh Token 검증 후 Access Token 재발급
    public String reIssueAccessToken(String refreshToken) {
        Claims claims = parseClaims(refreshToken);
        if (!"refresh".equals(claims.get("type"))) {
            throw new IllegalArgumentException("Invalid Refresh Token");
        }
        if (claims.getExpiration().before(new Date())) {
            throw new IllegalArgumentException("Refresh Token expired");
        }

        String userId = claims.getSubject();
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + accessTokenValidTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Refresh Token 복호화 및 검증
    public String validateAndDecryptRefreshToken(String encryptedRefreshToken) {
        try {

            log.info("📌 Validating and Decrypting Refresh Token: {}", encryptedRefreshToken);

            // Refresh Token 복호화
            String refreshToken = cryptoUtil.decrypt(encryptedRefreshToken);
            log.info("✅ Decrypted Refresh Token: {}", refreshToken);

            // Refresh Token 검증
            Claims claims = parseClaims(refreshToken);
            if (!"refresh".equals(claims.get("type"))) {
                throw new IllegalArgumentException("Invalid Refresh Token");
            }

            if (claims.getExpiration().before(new Date())) {
                throw new IllegalArgumentException("Refresh Token expired");
            }

            return refreshToken;
        } catch (Exception e) {
            log.error("❌ Invalid Refresh Token: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid or expired Refresh Token", e);
        }
    }

    // Claims 파싱
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
