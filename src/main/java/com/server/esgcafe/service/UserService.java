package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.user.UserSaveRequest;
import com.server.esgcafe.domain.dto.user.UserSaveResponse;
import com.server.esgcafe.domain.entity.User;
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

        log.info("🍞nickname : {}", request.getNickname());
        log.info("🍞nickname length : {}", request.getNickname().length());

        // 닉네임 길이 검증
        if (request.getNickname().length() > 5) {
            String errorMessage = "닉네임은 5글자 이하여야 합니다.";
            log.error("🍞Validation failed: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage); // 적절한 예외를 던진다.
        }

        // 닉네임의 각 문자와 유니코드 값을 출력
        for (int i = 0; i < request.getNickname().length(); i++) {
            char c = request.getNickname().charAt(i);
            log.info("🍞char at {}: '{}', Unicode: {}", i, c, (int) c);
        }

        Optional<User> existingUser = userRepository.findByNickName(request.getNickname());

        if (existingUser.isPresent()) {
            // 사용자가 이미 존재하는 경우 -> 로그인 처리
            log.info("🍞User '{}' found, logging in...", request.getNickname());
            User user = existingUser.get();
            return UserSaveResponse.from(user, "Login successful");
        } else {
            // 사용자가 존재하지 않는 경우 -> 회원 가입 처리
            log.info("🍞User '{}' not found, creating new user...", request.getNickname());
            User newUser = request.toEntity();
            User savedUser = userRepository.save(newUser);
            return UserSaveResponse.from(savedUser, "Signup successful");
        }
    }
}