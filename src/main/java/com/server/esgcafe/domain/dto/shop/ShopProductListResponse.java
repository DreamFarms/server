package com.server.esgcafe.domain.dto.shop;

import lombok.*;
import java.util.List;

@Getter
@Builder
public class ShopProductListResponse {

    private List<ShopProductResponse> shopProducts;

    public static ShopProductListResponse of(List<ShopProductResponse> products) {
        return ShopProductListResponse.builder()
                .shopProducts(products)
                .build();
    }

}
