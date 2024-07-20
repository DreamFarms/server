package com.server.esgcafe.domain.entity;

import com.server.esgcafe.domain.enum_class.RewardType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rewardNo;

    private String rewardName;
    private RewardType rewardType;

    private String description;

    @ManyToOne
    @JoinColumn(name = "miniGame_id")
    private MiniGame miniGame;

}
