package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.user.UserRequest;
import com.server.esgcafe.domain.dto.user.UserResponse;
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


    public UserResponse processUser(UserRequest request) {

        log.info("🍞nickname : {}", request.getNickname());

        Optional<User> existingUser = userRepository.findByNickName(request.getNickname());

        if (existingUser.isPresent()) {
            // 사용자가 이미 존재하는 경우
            throw new AppException(ErrorCode.DUPLICATED_NICKNAME);
        } else {
            // 사용자가 존재하지 않는 경우, 새로 생성
            User newUser = request.toEntity();
            User savedUser = userRepository.save(newUser);
            return UserResponse.from(savedUser.getNickName());
        }
    }
}