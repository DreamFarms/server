package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.shop.ShopProductListResponse;
import com.server.esgcafe.domain.dto.shop.ShopProductResponse;
import com.server.esgcafe.domain.dto.shop.ShopProductRewardResponse;
import com.server.esgcafe.domain.entity.ShopProduct;
import com.server.esgcafe.domain.entity.ShopProductReward;
import com.server.esgcafe.repository.ShopProductRepository;
import com.server.esgcafe.repository.ShopProductRewardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShopProductService {

    private final ShopProductRepository shopProductRepository;
    private final ShopProductRewardRepository shopProductRewardRepository;

    @Transactional(readOnly = true)
    public ShopProductListResponse getActiveProducts() {

        log.info("🍞 Shop product list load start");

        List<ShopProduct> shopProducts = shopProductRepository.findByActiveTrue();

        List<ShopProductResponse> responses = new ArrayList<>();

        for (ShopProduct product : shopProducts) {

            List<ShopProductReward> rewards =
                    shopProductRewardRepository.findByShopProduct(product);

            List<ShopProductRewardResponse> rewardResponses = rewards.stream()
                    .map(ShopProductRewardResponse::from)
                    .toList();

            responses.add(ShopProductResponse.from(product, rewardResponses));
        }

        log.info("🍞 Shop product list count: {}", responses.size());

        return ShopProductListResponse.of(responses);
    }
}
