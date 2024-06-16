package com.server.esgcafe.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MiniGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long miniGameNo;

    private String miniGameName;
    private int limitLevel;

    // 하나의 미니게임에서 여러 가지 보상 제공
    @OneToMany(mappedBy = "miniGame", cascade = CascadeType.ALL)
    private Set<MiniGameReward> rewards;

}
