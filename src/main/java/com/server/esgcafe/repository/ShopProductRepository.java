package com.server.esgcafe.repository;

import com.server.esgcafe.domain.entity.ShopProduct;
import com.server.esgcafe.domain.enum_class.ShopProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShopProductRepository extends JpaRepository<ShopProduct, Long> {

    Optional<ShopProduct> findByProductCode(String productCode);

    Optional<ShopProduct> findByGoogleProductId(String googleProductId);

    List<ShopProduct> findByActiveTrue();

    List<ShopProduct> findByProductTypeAndActiveTrue(ShopProductType productType);

    boolean existsByProductCode(String productCode);

    boolean existsByGoogleProductId(String googleProductId);

}
