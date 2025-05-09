package com.server.esgcafe.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserQuestProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userQuestProgressNo;

    private int completedCount;

    private LocalDateTime lastCompletedAt; // 마지막 완료 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_code")
    private Quest quest;

    @ManyToOne
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    public UserQuestProgress(User user, Quest quest, int completedCount, LocalDateTime lastCompletedAt) {
        this.user = user;
        this.quest = quest;
        this.completedCount = completedCount;
        this.lastCompletedAt = lastCompletedAt;
    }

    // 완료 횟수 증가 메서드
    public void incrementCompletedCount() {
        this.completedCount++;
    }

    // 마지막 완료 시간 갱신 메서드
    public void updateLastCompletedAt() {
        this.lastCompletedAt = LocalDateTime.now();
    }

}


