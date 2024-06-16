package com.server.esgcafe.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MiniGameReward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long miniGameRewardNo;

    @ManyToOne
    @JoinColumn(name = "reward_no")
    private Reward reward;

    private double probability; // 보상이 나오는 확률
}
