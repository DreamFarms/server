package com.server.esgcafe.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NpcPreferredFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long npcPreferredFoodNo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "npc_no", nullable = false, foreignKey = @ForeignKey(name = "fk_npf_npc"))
    private Npc npc;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "food_no", nullable = false, foreignKey = @ForeignKey(name = "fk_npf_food"))
    private Food food;

    @Column(nullable = false)
    private int sortOrder; // 노출 순서


}
