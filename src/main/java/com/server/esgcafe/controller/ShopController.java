package com.server.esgcafe.controller;

import com.server.esgcafe.domain.dto.shop.ShopCurrencyResponse;
import com.server.esgcafe.domain.dto.shop.ShopProductListResponse;
import com.server.esgcafe.domain.dto.shop.ShopPurchaseRequest;
import com.server.esgcafe.domain.dto.shop.ShopPurchaseResponse;
import com.server.esgcafe.exception.Response;
import com.server.esgcafe.service.ShopProductService;
import com.server.esgcafe.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopProductService shopProductService;
    private final ShopService shopService;

    // 상점 상품 리스트
    @GetMapping("/products")
    public Response<ShopProductListResponse> getProducts() {

        ShopProductListResponse response = shopProductService.getActiveProducts();
        return Response.success(response);
    }

    @GetMapping("/currency")
    public Response<ShopCurrencyResponse> getCurrency(@RequestParam String nickname) {

        ShopCurrencyResponse response = shopService.getMyCurrency(nickname);
        return Response.success(response);
    }

    @PostMapping("/purchase")
    public Response<ShopPurchaseResponse> purchase(@RequestBody ShopPurchaseRequest request) {

        ShopPurchaseResponse response = shopService.purchase(request);
        return Response.success(response);
    }

}
