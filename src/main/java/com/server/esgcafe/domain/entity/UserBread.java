package com.server.esgcafe.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserBread extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userBreadNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_no", nullable = false)
    private Food food;

    private int breadCount;

    public void updateBreadCount(int breadCount) {
        this.breadCount = breadCount;
    }

    public static UserBread update(UserBread existingUserBread, int additionalBreadCount) {
        existingUserBread.updateBreadCount(existingUserBread.getBreadCount() + additionalBreadCount);
        return existingUserBread;
    }

}
