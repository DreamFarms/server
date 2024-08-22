package com.server.esgcafe.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserReward extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    private String rewardName;
    private int rewardCount;

    public UserReward withUpdatedRewardCount(int newRewardCount) {
        return UserReward.builder()
                .id(this.id)
                .user(this.user)
                .rewardName(this.rewardName)
                .rewardCount(newRewardCount)
                .build();
    }

    // 개수 업데이트 메서드
    public void updateCount(int newCount) {
        this.rewardCount = newCount;
    }
}
