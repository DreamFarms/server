package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.userReward.UserRewardInfo;
import com.server.esgcafe.domain.dto.userReward.UserRewardSaveRequest;
import com.server.esgcafe.domain.dto.userReward.UserRewardSaveResponse;
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

import java.time.LocalDateTime;
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
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<UserRewardInfo> rewardInfos = request.getRewards();

        UserReward lastReward = null;

        for (int i = 0; i < rewardInfos.size(); i++) {

            UserRewardInfo userRewardInfo = rewardInfos.get(i);

            log.info("🍞 처리 중인 리워드 - 순서: {}, 리워드 이름: {}", i + 1, userRewardInfo.getName());

            UserReward existingUserReward = userRewardRepository.findByUserAndRewardName(user, userRewardInfo.getName())
                    .orElseThrow(() -> new AppException(ErrorCode.REWARD_NOT_FOUND));

            if (existingUserReward != null) {
                // 기존 리워드가 있을 경우, 개수 업데이트
                int updatedCount = existingUserReward.getRewardCount() + userRewardInfo.getCount();
                existingUserReward.updateCount(updatedCount);
                lastReward = userRewardRepository.save(existingUserReward);
            } else {
                // 기존 리워드가 없을 경우, 새 리워드 생성 및 저장
                UserReward newUserReward = userRewardInfo.toEntity(user);
                lastReward = userRewardRepository.save(newUserReward);
            }
        }

        // 응답 객체 생성
        boolean isSaved = lastReward != null;
        LocalDateTime saveTime = isSaved ? lastReward.getCreatedAt() : null;

        log.info("🍞 리워드 추가 완료");

        return new UserRewardSaveResponse(isSaved, saveTime);
    }


}
