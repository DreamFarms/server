package com.server.esgcafe.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Npc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long npcNo;

    // 종족
    private String tribe;

    // npc 특성
    private String npcTrait;

    // npc 스폰 확률
    private double spawnProbability;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_no")
    private Food food;

}
