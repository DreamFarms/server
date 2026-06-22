package com.server.esgcafe.repository;

import com.server.esgcafe.domain.entity.ShopProduct;
import com.server.esgcafe.domain.entity.ShopProductReward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopProductRewardRepository extends JpaRepository<ShopProductReward, Long> {

    List<ShopProductReward> findByShopProduct(ShopProduct shopProduct);
}