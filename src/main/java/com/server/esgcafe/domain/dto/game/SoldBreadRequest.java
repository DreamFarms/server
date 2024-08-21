package com.server.esgcafe.domain.dto.game;

import com.server.esgcafe.domain.entity.Food;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.FoodRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoldBreadRequest {

    private String foodName;
    private int soldCount;

    // 판매된 빵에 해당하는 Food 객체를 가져올 수 있도록 변경
    public Food getFood(FoodRepository foodRepository) {
        return foodRepository.findByName(this.foodName)
                .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));
    }
}
