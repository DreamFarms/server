package com.server.esgcafe.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserNpcProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNpcProgressNo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_no", nullable = false, foreignKey = @ForeignKey(name = "fk_unp_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "npc_no", nullable = false, foreignKey = @ForeignKey(name = "fk_unp_npc"))
    private Npc npc;

    @Column(nullable = false)
    private boolean unlocked;    // 방문 경험 여부

    @Column(nullable = false)
    private int visitCount;      // 방문 횟수(게이지)

    public void unlock() {
        this.unlocked = true;
    }

    public void increaseVisitCount() {
        this.visitCount++;
    }


}
