package com.server.esgcafe.domain.entity;

import com.server.esgcafe.domain.enum_class.ItemType;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserInventory extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userInventoryNo;

    @ManyToOne
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    private Long foodOrIngredientNo;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    private int count;

    public void updateCount(int count) {
        this.count = count;
    }

    // 재고 차감
    public void subtractInventoryCount(int soldCount) {
        if (this.count < soldCount) {
            throw new AppException(ErrorCode.NOT_ENOUGH_BREAD);
        }
        this.count -= soldCount;
    }


    @PrePersist
    public void validate() {
        if (foodOrIngredientNo == null || itemType == null) {
            throw new AppException(ErrorCode.INVALID_INVENTORY_ITEM);
        }
    }
}
