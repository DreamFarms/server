package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.shop.*;
import com.server.esgcafe.domain.entity.*;
import com.server.esgcafe.domain.enum_class.ItemType;
import com.server.esgcafe.domain.enum_class.PaymentCurrency;
import com.server.esgcafe.domain.enum_class.ShopRewardType;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopService {

    private final UserRepository userRepository;
    private final ShopProductRepository shopProductRepository;
    private final ShopProductRewardRepository shopProductRewardRepository;
    private final PurchaseTransactionRepository purchaseTransactionRepository;

    private final UserInventoryRepository userInventoryRepository;
    private final IngredientRepository ingredientRepository;

    @Transactional
    public ShopCurrencyResponse getMyCurrency(String nickname) {

        User user = userRepository.findByNickName(nickname)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return ShopCurrencyResponse.from(user);
    }

    // 인 게임 재화로 구매
    @Transactional
    public ShopPurchaseResponse purchase(ShopPurchaseRequest request) {

        log.info("[SHOP PURCHASE] 구매 요청 시작 - nickname: {}, productCode: {}",
                request.getNickname(),
                request.getProductCode());

        if (request.getProductCode() == null || request.getProductCode().isBlank()) {
            log.warn("[SHOP PURCHASE] 상품 코드 누락");
            throw new AppException(ErrorCode.INVALID_PRODUCT_CODE);
        }

        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        log.info("[SHOP PURCHASE] 유저 조회 성공 - userNo: {}, nickname: {}, gold: {}, cash: {}, ticket: {}",
                user.getUserNo(),
                user.getNickName(),
                user.getGold(),
                user.getCash(),
                user.getTicket());

        ShopProduct product = shopProductRepository.findByProductCode(request.getProductCode())
                .orElseThrow(() -> new AppException(ErrorCode.SHOP_PRODUCT_NOT_FOUND));

        log.info("[SHOP PURCHASE] 상품 조회 성공 - shopProductNo: {}, productCode: {}, paymentCurrency: {}, priceAmount: {}, active: {}",
                product.getShopProductNo(),
                product.getProductCode(),
                product.getPaymentCurrency(),
                product.getPriceAmount(),
                product.isActive());

        List<ShopProductReward> rewards = shopProductRewardRepository.findByShopProduct(product);

        log.info("[SHOP PURCHASE] 상품 보상 조회 성공 - productCode: {}, rewardCount: {}",
                product.getProductCode(),
                rewards.size());

        validateProduct(product, rewards);

        log.info("[SHOP PURCHASE] 결제 전 재화 - nickname: {}, gold: {}, cash: {}, ticket: {}",
                user.getNickName(),
                user.getGold(),
                user.getCash(),
                user.getTicket());

        payInGameCurrency(user, product);

        log.info("[SHOP PURCHASE] 결제 후 재화 - nickname: {}, paymentCurrency: {}, priceAmount: {}, gold: {}, cash: {}, ticket: {}",
                user.getNickName(),
                product.getPaymentCurrency(),
                product.getPriceAmount(),
                user.getGold(),
                user.getCash(),
                user.getTicket());

        PurchaseTransaction transaction = request.toTransactionEntity(user, product);
        PurchaseTransaction savedTransaction = purchaseTransactionRepository.save(transaction);

        log.info("[SHOP PURCHASE] 구매 기록 저장 완료 - purchaseTransactionNo: {}, productCode: {}, status: {}",
                savedTransaction.getPurchaseTransactionNo(),
                savedTransaction.getProductCode(),
                savedTransaction.getPurchaseStatus());

        grantRewards(user, rewards);

        savedTransaction.markRewardGranted();

        log.info("[SHOP PURCHASE] 보상 지급 완료 - purchaseTransactionNo: {}, status: {}",
                savedTransaction.getPurchaseTransactionNo(),
                savedTransaction.getPurchaseStatus());

        List<ShopProductRewardResponse> rewardResponses = rewards.stream()
                .map(ShopProductRewardResponse::from)
                .toList();

        log.info("[SHOP PURCHASE] 구매 완료 - nickname: {}, productCode: {}, finalGold: {}, finalCash: {}, finalTicket: {}",
                user.getNickName(),
                product.getProductCode(),
                user.getGold(),
                user.getCash(),
                user.getTicket());

        return ShopPurchaseResponse.of(savedTransaction, user, rewardResponses);
    }

    private void validateProduct(ShopProduct product, List<ShopProductReward> rewards) {

        log.info("[SHOP PURCHASE] 상품 검증 시작 - productCode: {}", product.getProductCode());

        if (!product.isActive()) {
            log.warn("[SHOP PURCHASE] 비활성 상품 구매 시도 - productCode: {}", product.getProductCode());
            throw new AppException(ErrorCode.SHOP_PRODUCT_INACTIVE);
        }

        if (product.getPaymentCurrency() == PaymentCurrency.REAL_MONEY) {
            log.warn("[SHOP PURCHASE] 실제 결제 상품을 인게임 재화 API로 구매 시도 - productCode: {}",
                    product.getProductCode());
            throw new AppException(ErrorCode.REAL_MONEY_PRODUCT);
        }

        if (product.getPriceAmount() <= 0) {
            log.warn("[SHOP PURCHASE] 상품 가격 오류 - productCode: {}, priceAmount: {}",
                    product.getProductCode(),
                    product.getPriceAmount());
            throw new AppException(ErrorCode.INVALID_SHOP_PRODUCT);
        }

        if (rewards == null || rewards.isEmpty()) {
            log.warn("[SHOP PURCHASE] 상품 보상 없음 - productCode: {}", product.getProductCode());
            throw new AppException(ErrorCode.SHOP_REWARD_NOT_FOUND);
        }

        log.info("[SHOP PURCHASE] 상품 검증 완료 - productCode: {}", product.getProductCode());
    }

    private void payInGameCurrency(User user, ShopProduct product) {

        log.info("[SHOP PURCHASE] 인게임 재화 결제 처리 - paymentCurrency: {}, priceAmount: {}",
                product.getPaymentCurrency(),
                product.getPriceAmount());

        if (product.getPaymentCurrency() == PaymentCurrency.GOLD) {
            user.useGold(product.getPriceAmount());
            return;
        }

        if (product.getPaymentCurrency() == PaymentCurrency.CASH) {
            user.useCash(product.getPriceAmount());
            return;
        }

        log.warn("[SHOP PURCHASE] 지원하지 않는 결제 재화 - productCode: {}, paymentCurrency: {}",
                product.getProductCode(),
                product.getPaymentCurrency());

        throw new AppException(ErrorCode.INVALID_SHOP_PRODUCT);
    }

    private void grantRewards(User user, List<ShopProductReward> rewards) {

        log.info("[SHOP PURCHASE] 보상 지급 시작 - rewardCount: {}", rewards.size());

        for (ShopProductReward reward : rewards) {

            log.info("[SHOP PURCHASE] 보상 지급 처리 - rewardType: {}, rewardCode: {}, rewardAmount: {}",
                    reward.getRewardType(),
                    reward.getRewardCode(),
                    reward.getRewardAmount());

            if (reward.getRewardType() == ShopRewardType.GOLD) {
                user.addGold(reward.getRewardAmount());

                log.info("[SHOP PURCHASE] GOLD 지급 완료 - amount: {}, currentGold: {}",
                        reward.getRewardAmount(),
                        user.getGold());
                continue;
            }

            if (reward.getRewardType() == ShopRewardType.CASH) {
                user.addCash(reward.getRewardAmount());

                log.info("[SHOP PURCHASE] CASH 지급 완료 - amount: {}, currentCash: {}",
                        reward.getRewardAmount(),
                        user.getCash());
                continue;
            }

            if (reward.getRewardType() == ShopRewardType.TICKET) {
                user.addTicket(reward.getRewardAmount());

                log.info("[SHOP PURCHASE] TICKET 지급 완료 - amount: {}, currentTicket: {}",
                        reward.getRewardAmount(),
                        user.getTicket());
                continue;
            }

            if (reward.getRewardType() == ShopRewardType.INGREDIENT) {
                grantIngredientReward(user, reward);
                continue;
            }

            log.warn("[SHOP PURCHASE] 지원하지 않는 보상 타입 - rewardType: {}, rewardCode: {}",
                    reward.getRewardType(),
                    reward.getRewardCode());

            throw new AppException(ErrorCode.INVALID_SHOP_REWARD);
        }

        log.info("[SHOP PURCHASE] 보상 지급 전체 완료");
    }

    private void grantIngredientReward(User user, ShopProductReward reward) {

        log.info("[SHOP PURCHASE] 재료 보상 지급 시작 - ingredientName: {}, amount: {}",
                reward.getRewardCode(),
                reward.getRewardAmount());

        Ingredient ingredient = ingredientRepository.findByName(reward.getRewardCode())
                .orElseThrow(() -> new AppException(ErrorCode.INGREDIENT_NOT_FOUND));

        log.info("[SHOP PURCHASE] 재료 조회 성공 - ingredientNo: {}, name: {}",
                ingredient.getIngredientNo(),
                ingredient.getName());

        UserInventory userInventory = userInventoryRepository
                .findByUserAndFoodOrIngredientNoAndItemType(
                        user,
                        ingredient.getIngredientNo(),
                        ItemType.INGREDIENT
                )
                .orElse(null);

        if (userInventory == null) {
            ShopInventoryRewardInfo info = new ShopInventoryRewardInfo(
                    ingredient.getIngredientNo(),
                    ItemType.INGREDIENT,
                    reward.getRewardAmount()
            );

            userInventoryRepository.save(info.toEntity(user));

            log.info("[SHOP PURCHASE] 신규 재료 인벤토리 생성 - ingredientNo: {}, name: {}, count: {}",
                    ingredient.getIngredientNo(),
                    ingredient.getName(),
                    reward.getRewardAmount());
            return;
        }

        int beforeCount = userInventory.getCount();

        userInventory.addInventoryCount(reward.getRewardAmount());
        userInventoryRepository.save(userInventory);

        log.info("[SHOP PURCHASE] 기존 재료 인벤토리 증가 - ingredientNo: {}, name: {}, beforeCount: {}, addCount: {}, afterCount: {}",
                ingredient.getIngredientNo(),
                ingredient.getName(),
                beforeCount,
                reward.getRewardAmount(),
                userInventory.getCount());
    }

    // 실물 재화로 구매
    // google play store 연결 및 카드 결제 연결 필요

}




