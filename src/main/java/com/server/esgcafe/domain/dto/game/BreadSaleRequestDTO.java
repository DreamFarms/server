package com.server.esgcafe.domain.dto.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BreadSaleRequestDTO {

    private String nickname;
//    private Long npcId;  // 어떤 NPC가 사갔는지
    private String name; // 🥖 구매한 빵
    private int quantity; // 🥖 구매 개수
}