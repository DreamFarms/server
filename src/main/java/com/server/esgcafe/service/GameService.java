package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.game.*;
import com.server.esgcafe.domain.entity.Food;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserInventory;
import com.server.esgcafe.domain.enum_class.ItemType;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.FoodRepository;
import com.server.esgcafe.repository.UserInventoryRepository;
import com.server.esgcafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GameService {

    private final EconomyRedisService economyRedisService;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserInventoryRepository userInventoryRepository;

    private static String goldKey(Long userNo) {
        return "wallet:gold:" + userNo;
    }

    private static String cashKey(Long userNo) {
        return "wallet:cash:" + userNo;
    }

    private static String invFoodKey(Long userNo) {
        return "inv:food:" + userNo;
    }

    private static String deltaGoldKey() {
        return "delta:gold";
    }

    private static String deltaCashKey() {
        return "delta:cash";
    }

    private static String deltaInvFoodKey(Long userNo) {
        return "delta:inv:food:" + userNo;
    }


    @Transactional
    public StoreEnterResponse enterStore(StoreEnterRequest request) {
        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Long userNo = user.getUserNo();

        // 1. 해당 유저의 Redis delta를 먼저 MySQL에 반영
        flushUserDeltaToMysql(userNo);

        // 2. MySQL 최신값 다시 조회
        User refreshedUser = userRepository.findById(userNo)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 3. Redis gold / cash 초기화
        redisTemplate.opsForValue().set(goldKey(userNo), String.valueOf(refreshedUser.getGold()));
        redisTemplate.opsForValue().set(cashKey(userNo), String.valueOf(refreshedUser.getCash()));

        // 4. MySQL 인벤(빵만) 조회
        List<UserInventory> breadInventories =
                userInventoryRepository.findByUserAndItemType(refreshedUser, ItemType.FOOD);

        // 5. Redis 인벤 초기화 (기존 값 삭제 후 MySQL 기준으로 다시 세팅)
        redisTemplate.delete(invFoodKey(userNo));

        for (UserInventory inventory : breadInventories) {
            redisTemplate.opsForHash().put(
                    invFoodKey(userNo),
                    String.valueOf(inventory.getFoodOrIngredientNo()),
                    String.valueOf(inventory.getCount())
            );
        }

        // 6. 응답용 bread list 생성
        List<StoreEnterResponse.BreadInfo> breads = new ArrayList<>();

        for (UserInventory inventory : breadInventories) {
            Food food = foodRepository.findById(inventory.getFoodOrIngredientNo())
                    .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

            breads.add(new StoreEnterResponse.BreadInfo(
                    food.getName(),
                    inventory.getCount()
            ));
        }

        return new StoreEnterResponse(
                refreshedUser.getGold(),
                refreshedUser.getCash(),
                breads
        );
    }


    public SellBreadResponse sellBreadNow(BreadSaleRequest request) {
        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Long userNo = user.getUserNo();

        long currentGold = 0;

        for (BreadSaleItem item : request.getBreadList()) {

            Food food = foodRepository.findByName(item.getName())
                    .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

            Long foodNo = food.getFoodNo();
            int qty = item.getQuantity();
            long goldInc = (long) food.getPrice() * qty;

            List<?> res = economyRedisService.sellBread(userNo, foodNo, qty, goldInc);

            long ok = ((Number) res.get(0)).longValue();
            if (ok != 1L) {
                throw new AppException(ErrorCode.NOT_ENOUGH_BREAD);
            }

            currentGold = ((Number) res.get(2)).longValue();
        }

        return new SellBreadResponse(currentGold);
    }

    @Transactional
    public void flushUserDeltaToMysql(Long userNo) {

        // 1. gold delta 반영
        Object goldDeltaObj = redisTemplate.opsForHash().get(deltaGoldKey(), String.valueOf(userNo));
        if (goldDeltaObj != null) {
            long deltaGold = Long.parseLong(goldDeltaObj.toString());

            User user = userRepository.findById(userNo)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            user.addGold(deltaGold);
            userRepository.save(user);

            redisTemplate.opsForHash().delete(deltaGoldKey(), String.valueOf(userNo));
        }

        // 2. inventory delta 반영
        String invDeltaKey = deltaInvFoodKey(userNo);
        Map<Object, Object> invDeltas = redisTemplate.opsForHash().entries(invDeltaKey);

        if (invDeltas != null && !invDeltas.isEmpty()) {
            User user = userRepository.findById(userNo)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            for (Map.Entry<Object, Object> entry : invDeltas.entrySet()) {
                Long foodNo = Long.parseLong(entry.getKey().toString());
                int deltaCount = Integer.parseInt(entry.getValue().toString());

                UserInventory inventory = userInventoryRepository
                        .findByUserAndFoodOrIngredientNoAndItemType(user, foodNo, ItemType.FOOD)
                        .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

                inventory.addInventoryCount(deltaCount);
                userInventoryRepository.save(inventory);
            }

            redisTemplate.delete(invDeltaKey);
        }
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void flushDeltaToMysql() {

        // 1️⃣ 골드 델타 반영
        Map<Object, Object> goldDeltas =
                redisTemplate.opsForHash().entries("delta:gold");

        for (Map.Entry<Object, Object> entry : goldDeltas.entrySet()) {

            Long userNo = Long.parseLong(entry.getKey().toString());
            Long deltaGold = Long.parseLong(entry.getValue().toString());

            userRepository.findById(userNo).ifPresent(user -> {
                user.addGold(deltaGold);
                userRepository.save(user);
            });

            redisTemplate.opsForHash().delete("delta:gold", userNo.toString());
        }

        // 2️⃣ 인벤 델타 반영
        Set<String> keys = redisTemplate.keys("delta:inv:food:*");

        if (keys == null) return;

        for (String key : keys) {

            String userNoStr = key.split(":")[3];
            Long userNo = Long.parseLong(userNoStr);

            Map<Object, Object> invDeltas =
                    redisTemplate.opsForHash().entries(key);

            userRepository.findById(userNo).ifPresent(user -> {

                for (Map.Entry<Object, Object> entry : invDeltas.entrySet()) {

                    Long foodNo = Long.parseLong(entry.getKey().toString());
                    int deltaCount = Integer.parseInt(entry.getValue().toString());

                    userInventoryRepository
                            .findByUserAndFoodOrIngredientNoAndItemType(
                                    user, foodNo, ItemType.FOOD)
                            .ifPresent(inventory -> {
                                inventory.addInventoryCount(deltaCount);
                                userInventoryRepository.save(inventory);
                            });
                }
            });

            redisTemplate.delete(key);
        }
    }

}