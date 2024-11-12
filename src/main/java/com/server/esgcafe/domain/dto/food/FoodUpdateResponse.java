package com.server.esgcafe.domain.dto.food;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodUpdateResponse {

    private boolean isSaved; // 저장 완료 여부
    private LocalDateTime saveTime; // 저장 시간

}
