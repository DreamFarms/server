package com.server.esgcafe.domain.dto.userInventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BreadInfo {

    private String name;  // 빵의 이름 (foodOrIngredientNo에 해당하는 정보)
    private int count;    // 빵의 개수

}
