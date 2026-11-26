package com.server.esgcafe.domain.entity;

import com.server.esgcafe.domain.enum_class.FurnitureType;
import com.server.esgcafe.domain.enum_class.ThemeCode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_customization_user_part",
                columnNames = {"user_no", "theme_part_type"}
        )
)
public class UserCustomization extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCustomizationNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "theme_part_type", nullable = false)
    private FurnitureType themePartType;

    @Enumerated(EnumType.STRING)
    @Column(name = "theme_code", nullable = false)
    private ThemeCode themeCode;

    public void updateThemeCode(ThemeCode themeCode) {
        this.themeCode = themeCode;
    }
}
