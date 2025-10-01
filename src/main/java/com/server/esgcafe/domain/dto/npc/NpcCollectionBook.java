package com.server.esgcafe.domain.dto.npc;

import lombok.*;

import java.util.List;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NpcCollectionBook {

    private String name;  // 손님 이름
    private String intro; // 손님 소개
    private List<String> preferredBreads; // 선호하는 빵
    private boolean preferredBreadsVisible; // 선호 빵 열렸는지 여부
    private int visitCount; // 손님 게이지
    private boolean unlocked; // 손님 해금 여부

}
