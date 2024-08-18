package com.server.esgcafe.domain.dto.food;

import com.server.esgcafe.domain.entity.UserBread;
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

    public FoodUpdateResponse(UserBread userBread) {
        this.isSaved = true;
        this.saveTime = (userBread != null && userBread.getCreatedAt() != null)
                ? userBread.getCreatedAt()
                : LocalDateTime.now(); // userBread가 null인 경우 현재 시간 사용
    }
}
