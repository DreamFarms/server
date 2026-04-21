package com.server.esgcafe.domain.entity;

import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StoreDisplay extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeDisplayNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    private Integer tableNo;
    private Integer slotNo;
    private Long foodNo;
    private Integer count;

    public void addCount(int amount) {
        this.count += amount;

        if (this.count < 0) {
            throw new AppException(ErrorCode.NOT_ENOUGH_BREAD);
        }
    }

    public void subtractCount(int amount) {
        if (this.count < amount) {
            throw new AppException(ErrorCode.NOT_ENOUGH_BREAD);
        }
        this.count -= amount;
    }

    public void changeFood(Long foodNo, Integer count) {
        this.foodNo = foodNo;
        this.count = count;
    }
}
