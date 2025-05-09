package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.quest.QuestCompleteRequest;
import com.server.esgcafe.domain.dto.quest.QuestCompletionResponse;
import com.server.esgcafe.domain.dto.quest.UserQuestProgressDTO;
import com.server.esgcafe.domain.entity.Quest;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserQuestProgress;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.QuestRepository;
import com.server.esgcafe.repository.UserQuestProgressRepository;
import com.server.esgcafe.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestService {

    private final QuestRepository questRepository;
    private final UserQuestProgressRepository userQuestProgressRepository;
    private final UserRepository userRepository;

    // 일단은 클라에서 받아서 저장만
    // 나중에 더블 체크 추가

    @Transactional
    // 퀘스트 진행 업데이트
    public QuestCompletionResponse saveQuestCompletion(QuestCompleteRequest request) {

        log.info("📜 퀘스트 완료 요청: {}, {}", request.getNickname(), request.getQuestCode());

        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Quest quest = questRepository.findByQuestCode(request.getQuestCode())
                .orElseThrow(() -> new AppException(ErrorCode.QUEST_NOT_FOUND));

        UserQuestProgress userQuestProgress = userQuestProgressRepository.findByUserAndQuest(user, quest)
                .orElse(null);

        if (userQuestProgress != null) {

            userQuestProgress.incrementCompletedCount();
            userQuestProgress.updateLastCompletedAt();
            userQuestProgressRepository.save(userQuestProgress);
            log.info("📜 기존 퀘스트 진행도 업데이트 완료");

        } else {

            UserQuestProgressDTO questProgressDTO = new UserQuestProgressDTO(
                    1,
                    LocalDateTime.now(),
                    quest
            );
            userQuestProgress = questProgressDTO.toEntity(user);
            userQuestProgressRepository.save(userQuestProgress);
            log.info("📜퀘스트 저장 완료");

        }

        return new QuestCompletionResponse(quest.getQuestCode(), userQuestProgress.getCompletedCount());

    }

}
