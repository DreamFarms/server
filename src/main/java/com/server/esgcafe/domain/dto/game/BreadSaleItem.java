package com.server.esgcafe.domain.dto.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BreadSaleItem {

    private String name; // 구매한 빵
    private int quantity; // 구매 개수

}
