package com.server.esgcafe.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MiniGameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mini_game_id")
    private MiniGame miniGame;

    @Column(name = "player_id")
    private Long playerId;

    @Column(name = "player_score")
    private Integer playerScore;

    @ManyToOne
    @JoinColumn(name = "mini_game_reward_id")
    private MiniGameReward miniGameReward;

    @Column(name = "play_time")
    private LocalDateTime playTime;
}
