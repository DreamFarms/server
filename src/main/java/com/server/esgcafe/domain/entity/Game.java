package com.server.esgcafe.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameNo;

    private int level;

    private int currency;

    private int cash;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Food> foods; // 게임에서 보유한 음식 목록

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Ingredient> ingredients; // 게임에서 보유한 재료 목록

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;

}
