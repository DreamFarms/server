package com.server.esgcafe.domain.entity;

import com.server.esgcafe.domain.enum_class.KneadingGrade;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class KneadingAttempt extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attemptNo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "food_no", nullable = false)
    private Food food;

    @Enumerated(EnumType.STRING)
    private KneadingGrade grade;

    public void complete(KneadingGrade grade) {
        this.grade = grade;
    }
}
