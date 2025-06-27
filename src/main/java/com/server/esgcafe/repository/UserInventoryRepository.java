package com.server.esgcafe.repository;

import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserInventory;
import com.server.esgcafe.domain.enum_class.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInventoryRepository extends JpaRepository<UserInventory, Long> {

    List<UserInventory> findByUser(User user);

    // 사용자, 아이템 ID, 아이템 타입을 기준으로 UserInventory 조회
    @Query("SELECT ui FROM UserInventory ui WHERE ui.user = :user AND ui.foodOrIngredientNo = :foodOrIngredientNo AND ui.itemType = :itemType")
    Optional<UserInventory> findByUserAndFoodOrIngredientNoAndItemType(
            @Param("user") User user,
            @Param("foodOrIngredientNo") Long foodOrIngredientNo,
            @Param("itemType") ItemType itemType);

    List<UserInventory> findByUserAndItemType(User user, ItemType itemType);

}
