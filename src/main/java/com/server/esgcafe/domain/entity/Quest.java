package com.server.esgcafe.domain.entity;

import com.server.esgcafe.domain.enum_class.QuestType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questNo;

    private Long questCode;

    private String title;

    @Enumerated(EnumType.STRING)
    private QuestType questType;

    private LocalDateTime updatedAt; // 퀘스트 업데이트 시간

}
