package com.server.esgcafe.configuration.jwt;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
public class CryptoUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    private final SecretKey secretKey;

    // 생성자: SecretKey 또는 키 바이트를 입력받아 처리
    public CryptoUtil(byte[] keyBytes) {
        this.secretKey = validateAndCreateKey(keyBytes);
    }

    // AES 키 유효성 검사 및 SecretKey 생성
    private SecretKey validateAndCreateKey(byte[] keyBytes) {
        if (keyBytes.length != 16 && keyBytes.length != 24 && keyBytes.length != 32) {
            throw new IllegalArgumentException("Invalid AES key length: " + keyBytes.length + " bytes. Must be 16, 24, or 32 bytes.");
        }
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }


    public String encrypt(String data) {
        try {

            if (data == null || data.isEmpty()) {
                throw new IllegalArgumentException("❌ Data to encrypt cannot be null or empty");
            }

            log.info("🔐 Encrypting data: {}", data); // 디버깅 정보

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes("UTF-8"));

            String encoded = Base64.getEncoder().encodeToString(encryptedBytes);
            log.info("✅ Encryption successful. Encrypted Token: {}", encoded);
            return encoded;
        } catch (Exception e) {
            log.error("❌ Error while encrypting", e);
            throw new RuntimeException("Error while encrypting", e);
        }
    }

    public String decrypt(String encryptedData) {
        try {
            if (encryptedData == null || encryptedData.isEmpty()) {
                throw new IllegalArgumentException("❌ Encrypted data cannot be null or empty");
            }

            log.info("🔓 Decrypting data: {}", encryptedData); // 디버깅 정보

            // 🔹 앞뒤에 불필요한 쌍따옴표가 있는지 확인 후 제거
            if (encryptedData.startsWith("\"") && encryptedData.endsWith("\"")) {
                log.warn("⚠️ Detected leading and trailing quotes in token. Removing them.");
                encryptedData = encryptedData.substring(1, encryptedData.length() - 1);
            }

            // Base64 인코딩 변형 방지 처리
            String sanitizedToken = encryptedData.replace(" ", "+").replace("-", "+").replace("_", "/");
            log.info("📌 Sanitized Token Before Base64 Decode: {}", sanitizedToken);

            byte[] decodedData = Base64.getDecoder().decode(sanitizedToken);
            log.info("✅ Base64 Decoding Successful. Decoded Length: {}", decodedData.length);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(decodedData);

            String decryptedText = new String(decryptedBytes, StandardCharsets.UTF_8);
            log.info("✅ Decryption successful. Decrypted Text: {}", decryptedText);
            return decryptedText;
        } catch (Exception e) {
            log.error("❌ Error while decrypting", e);
            throw new RuntimeException("Error while decrypting", e);
        }
    }

}
