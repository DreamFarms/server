package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.user.UserSaveRequest;
import com.server.esgcafe.domain.dto.user.UserSaveResponse;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;


    public UserSaveResponse processUser(UserSaveRequest request) {

        String nickname = request.getNickname().trim(); // 앞뒤 공백 제거
        nickname = nickname.replaceAll("[\\p{Zs}\\u200B]", "");  // ZWS를 빈 문자열로 대체

        log.info("🍞nickname : {}", nickname);
        log.info("🍞nickname length : {}", nickname.length());

        // 닉네임의 각 문자와 유니코드 값을 출력
        for (int i = 0; i < nickname.length(); i++) {
            char c = nickname.charAt(i);
            log.info("🍞char at {}: '{}', Unicode: {}", i, c, (int) c);
        }

        // 닉네임 길이 검증
        if (nickname.length() > 5) {
            String errorMessage = "닉네임은 5글자 이하여야 합니다.";
            log.error("🍞Validation failed: {}", errorMessage);
            throw new AppException(ErrorCode.INVALID_NICKNAME_LENGTH);
        }

        Optional<User> existingUser = userRepository.findByNickName(nickname);

        if (existingUser.isPresent()) {
            // 사용자가 이미 존재하는 경우 -> 로그인 처리
            log.info("🍞User '{}' found, logging in...", nickname);
            User user = existingUser.get();
            return UserSaveResponse.from(user, "Login successful");
        } else {
            // 사용자가 존재하지 않는 경우 -> 회원 가입 처리
            log.info("🍞User '{}' not found, creating new user...", nickname);
            User newUser = request.toEntity();
            User savedUser = userRepository.save(newUser);
            return UserSaveResponse.from(savedUser, "Signup successful");
        }
    }
}