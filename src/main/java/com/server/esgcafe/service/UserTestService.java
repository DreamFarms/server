package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.user.UserSaveRequest;
import com.server.esgcafe.domain.dto.user.UserSaveResponse;
import com.server.esgcafe.domain.dto.user.UserSaveTestResponse;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserTestService {

    private final UserRepository userRepository;


    public UserSaveTestResponse processTempUserByNickname(UserSaveRequest request) {

        String nickname = normalizeNickname(request.getNickname());

        log.info("임시 닉네임 회원가입/로그인 요청 - nickname: {}", nickname);
        log.info("임시 닉네임 길이 - length: {}", nickname.length());

        validateNicknameLength(nickname);

        Optional<User> optionalUser = userRepository.findByNickName(nickname);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            log.info("기존 유저 로그인 처리 - userNo: {}, nickname: {}",
                    user.getUserNo(),
                    user.getNickName());

            user.updateLoginTime(Instant.now());
            userRepository.save(user);

            return UserSaveTestResponse.from(user, "로그인되었습니다.");
        }

        request.setNickname(nickname);

        User newUser = request.toEntity();
        newUser.updateLoginTime(Instant.now());

        User savedUser = userRepository.save(newUser);

        log.info("신규 유저 임시 회원가입 완료 - userNo: {}, nickname: {}",
                savedUser.getUserNo(),
                savedUser.getNickName());

        return UserSaveTestResponse.from(savedUser, "회원가입이 완료되었습니다.");
    }

    private String normalizeNickname(String nickname) {

        if (nickname == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        String normalizedNickname = nickname.trim();
        normalizedNickname = normalizedNickname.replaceAll("[\\p{Zs}\\u200B]", "");

        return normalizedNickname;
    }

    private void validateNicknameLength(String nickname) {

        if (nickname.isBlank()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        if (nickname.length() > 8) {
            log.error("닉네임 길이 검증 실패 - nickname: {}, length: {}",
                    nickname,
                    nickname.length());

            throw new AppException(ErrorCode.INVALID_NICKNAME_LENGTH);
        }
    }

}