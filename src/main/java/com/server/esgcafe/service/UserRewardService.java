package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.UserReward.UserRewardInfo;
import com.server.esgcafe.domain.dto.UserReward.UserRewardSaveRequest;
import com.server.esgcafe.domain.dto.UserReward.UserRewardSaveResponse;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserReward;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.UserRepository;
import com.server.esgcafe.repository.UserRewardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRewardService {

    private final UserRewardRepository userRewardRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserRewardSaveResponse userRewardSave(UserRewardSaveRequest request) {

        log.info("🍞UserReward 저장 시작");

        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        UserRewardSaveResponse response = null;

        List<UserRewardInfo> rewardInfos = request.getRewards();

        for (int i = 0; i < rewardInfos.size(); i++) {

            UserRewardInfo userRewardInfo = rewardInfos.get(i);

            log.info("🍞 처리 중인 리워드 - 순서: {}, 리워드 이름: {}", i + 1, userRewardInfo.getName());

            UserReward userReward = userRewardInfo.toEntity(user);
            userRewardRepository.save(userReward);

            response = new UserRewardSaveResponse(userReward);
        }

        log.info("🍞 리워드 추가 완료");

        return response;
    }


}
