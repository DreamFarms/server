package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.test.TestCurrencyGrantRequest;
import com.server.esgcafe.domain.dto.test.TestCurrencyGrantResponse;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {

    private final UserRepository userRepository;

    @Transactional
    public TestCurrencyGrantResponse grantTestCurrency(TestCurrencyGrantRequest request) {

        log.info("[TEST CURRENCY] 테스트 재화 지급 요청 - nickname: {}, goldAmount: {}, cashAmount: {}",
                request.getNickname(),
                request.getGoldAmount(),
                request.getCashAmount());

        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        log.info("[TEST CURRENCY] 지급 전 재화 - nickname: {}, gold: {}, cash: {}, ticket: {}",
                user.getNickName(),
                user.getGold(),
                user.getCash(),
                user.getTicket());

        user.addGold(request.getGoldAmount());
        user.addCash(request.getCashAmount());

        log.info("[TEST CURRENCY] 지급 후 재화 - nickname: {}, gold: {}, cash: {}, ticket: {}",
                user.getNickName(),
                user.getGold(),
                user.getCash(),
                user.getTicket());

        return TestCurrencyGrantResponse.from(user);
    }

}
