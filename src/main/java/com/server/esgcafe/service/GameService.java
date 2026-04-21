package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.game.*;
import com.server.esgcafe.domain.dto.storeDisplay.StorePlaceRequest;
import com.server.esgcafe.domain.dto.storeDisplay.StorePlaceResponse;
import com.server.esgcafe.domain.dto.storeDisplay.StoreRemoveRequest;
import com.server.esgcafe.domain.dto.storeDisplay.StoreRemoveResponse;
import com.server.esgcafe.domain.entity.Food;
import com.server.esgcafe.domain.entity.StoreDisplay;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserInventory;
import com.server.esgcafe.domain.enum_class.ItemType;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.FoodRepository;
import com.server.esgcafe.repository.StoreDisplayRepository;
import com.server.esgcafe.repository.UserInventoryRepository;
import com.server.esgcafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {

    private final EconomyRedisService economyRedisService;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserInventoryRepository userInventoryRepository;
    private final StoreDisplayRepository storeDisplayRepository;

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

    private static String displaySlotKey(Long userNo) {
        return "display:slot:" + userNo;
    }

    private static String deltaDisplayKey(Long userNo) {
        return "delta:display:" + userNo;
    }


    @Transactional
    public StoreEnterResponse enterStore(StoreEnterRequest request) {

        log.info("🍞매장 입장");


        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Long userNo = user.getUserNo();

        // 1. 해당 유저 delta 먼저 MySQL 반영
        flushUserDeltaToMysql(userNo);

        // 2. MySQL 최신값 다시 조회
        User refreshedUser = userRepository.findById(userNo)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 3. Redis gold / cash 초기화
        redisTemplate.opsForValue().set(goldKey(userNo), String.valueOf(refreshedUser.getGold()));
        redisTemplate.opsForValue().set(cashKey(userNo), String.valueOf(refreshedUser.getCash()));

        // 4. inventory 조회 및 Redis 적재
        List<UserInventory> breadInventories =
                userInventoryRepository.findByUserAndItemType(refreshedUser, ItemType.FOOD);

        redisTemplate.delete(invFoodKey(userNo));

        List<StoreEnterResponse.InventoryBreadInfo> inventoryBreads = new ArrayList<>();

        for (UserInventory inventory : breadInventories) {
            redisTemplate.opsForHash().put(
                    invFoodKey(userNo),
                    String.valueOf(inventory.getFoodOrIngredientNo()),
                    String.valueOf(inventory.getCount())
            );

            Food food = foodRepository.findById(inventory.getFoodOrIngredientNo())
                    .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

            inventoryBreads.add(new StoreEnterResponse.InventoryBreadInfo(
                    food.getName(),
                    inventory.getCount()
            ));
        }

        // 5. display 조회 및 Redis 적재
        List<StoreDisplay> displays = storeDisplayRepository.findByUser(refreshedUser);

        redisTemplate.delete(displaySlotKey(userNo));

        List<StoreEnterResponse.DisplayBreadInfo> displayBreads = new ArrayList<>();

        for (StoreDisplay display : displays) {
            String field = display.getTableNo() + ":" + display.getSlotNo();
            String value = display.getFoodNo() + "," + display.getCount();

            redisTemplate.opsForHash().put(
                    displaySlotKey(userNo),
                    field,
                    value
            );

            Food food = foodRepository.findById(display.getFoodNo())
                    .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

            displayBreads.add(new StoreEnterResponse.DisplayBreadInfo(
                    display.getTableNo(),
                    display.getSlotNo(),
                    food.getName(),
                    display.getCount()
            ));
        }

        log.info("🍞매장 입장 통신 완료");

        return new StoreEnterResponse(
                refreshedUser.getGold(),
                refreshedUser.getCash(),
                inventoryBreads,
                displayBreads
        );
    }

    @Transactional
    public SellBreadResponse sellBreadNow(BreadSaleRequest request) {

        log.info("🍞 빵 판매 통신 시작");

        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (request.getSales() == null || request.getSales().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        long currentGold = 0L;
        List<SellBreadResponse.SoldSlotInfo> soldSlots = new ArrayList<>();

        for (BreadSaleSlotItem item : request.getSales()) {
            if (item.getTableNo() == null || item.getSlotNo() == null || item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }

            String rawSlot = economyRedisService.getDisplaySlotRaw(
                    user.getUserNo(),
                    item.getTableNo(),
                    item.getSlotNo()
            );

            if (rawSlot == null) {
                throw new AppException(ErrorCode.FOOD_NOT_FOUND);
            }

            String[] parts = rawSlot.split(",");
            Long foodNo = Long.parseLong(parts[0]);

            Food food = foodRepository.findById(foodNo)
                    .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

            long goldInc = (long) food.getPrice() * item.getQuantity();

            List<?> res = economyRedisService.sellBread(
                    user.getUserNo(),
                    item.getTableNo(),
                    item.getSlotNo(),
                    foodNo,
                    item.getQuantity(),
                    goldInc
            );

            long ok = ((Number) res.get(0)).longValue();
            if (ok != 1L) {
                throw new AppException(ErrorCode.NOT_ENOUGH_BREAD);
            }

            int remainCount = ((Number) res.get(1)).intValue();
            currentGold = ((Number) res.get(2)).longValue();

            soldSlots.add(new SellBreadResponse.SoldSlotInfo(
                    item.getTableNo(),
                    item.getSlotNo(),
                    food.getName(),
                    remainCount
            ));
        }

        log.info("🍞 빵 판매 통신 완료");

        return new SellBreadResponse(currentGold, soldSlots);
    }

    @Transactional
    public StorePlaceResponse placeBread(StorePlaceRequest request) {

        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        if (request.getTableNo() == null || request.getSlotNo() == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        Food food = foodRepository.findByName(request.getName())
                .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

        List<?> res = economyRedisService.placeBread(
                user.getUserNo(),
                request.getTableNo(),
                request.getSlotNo(),
                food.getFoodNo(),
                request.getQuantity()
        );

        long ok = ((Number) res.get(0)).longValue();
        if (ok != 1L) {
            long code = ((Number) res.get(1)).longValue();
            if (code == -2L) {
                throw new AppException(ErrorCode.NOT_ENOUGH_BREAD);
            }
            if (code == -3L) {
                throw new AppException(ErrorCode.INVALID_REQUEST); // 다른 빵이 이미 있음
            }
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        int inventoryCount = ((Number) res.get(1)).intValue();
        int slotCount = ((Number) res.get(2)).intValue();

        return new StorePlaceResponse(
                request.getTableNo(),
                request.getSlotNo(),
                food.getName(),
                slotCount,
                inventoryCount
        );
    }

    @Transactional
    public StoreRemoveResponse removeBread(StoreRemoveRequest request) {

        log.info("🍞 빵 슬롯에서 제거 통신 시작");

        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        if (request.getTableNo() == null || request.getSlotNo() == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        List<?> res = economyRedisService.removeBread(
                user.getUserNo(),
                request.getTableNo(),
                request.getSlotNo(),
                request.getQuantity()
        );

        long ok = ((Number) res.get(0)).longValue();
        if (ok != 1L) {
            long code = ((Number) res.get(1)).longValue();
            if (code == -2L) {
                throw new AppException(ErrorCode.FOOD_NOT_FOUND); // 빈 슬롯
            }
            if (code == -3L) {
                throw new AppException(ErrorCode.NOT_ENOUGH_BREAD);
            }
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        Long foodNo = ((Number) res.get(1)).longValue();
        int remainSlotCount = ((Number) res.get(2)).intValue();
        int inventoryCount = ((Number) res.get(3)).intValue();

        Food food = foodRepository.findById(foodNo)
                .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

        log.info("✅ 회수 성공 - userNo: {}, tableNo: {}, slotNo: {}, foodName: {}, removedQty: {}, remainSlotCount: {}, inventoryCount: {}",
                user.getUserNo(),
                request.getTableNo(),
                request.getSlotNo(),
                food.getName(),
                request.getQuantity(),
                remainSlotCount,
                inventoryCount);

        return new StoreRemoveResponse(
                request.getTableNo(),
                request.getSlotNo(),
                food.getName(),
                remainSlotCount,
                inventoryCount
        );
    }

    @Transactional
    public void flushUserDeltaToMysql(Long userNo) {

        Object goldDeltaObj = redisTemplate.opsForHash().get(deltaGoldKey(), String.valueOf(userNo));
        if (goldDeltaObj != null) {
            long deltaGold = Long.parseLong(goldDeltaObj.toString());

            User user = userRepository.findById(userNo)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            user.addGold(deltaGold);
            userRepository.save(user);

            redisTemplate.opsForHash().delete(deltaGoldKey(), String.valueOf(userNo));
        }

        syncInventoryFromRedis(userNo);
        syncDisplayFromRedis(userNo);
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void flushDeltaToMysql() {

        Map<Object, Object> goldDeltas = redisTemplate.opsForHash().entries(deltaGoldKey());
        for (Map.Entry<Object, Object> entry : goldDeltas.entrySet()) {
            Long userNo = Long.parseLong(entry.getKey().toString());
            Long deltaGold = Long.parseLong(entry.getValue().toString());

            userRepository.findById(userNo).ifPresent(user -> {
                user.addGold(deltaGold);
                userRepository.save(user);
            });

            redisTemplate.opsForHash().delete(deltaGoldKey(), userNo.toString());
        }

        Set<String> invKeys = redisTemplate.keys("delta:inv:food:*");
        if (invKeys != null) {
            for (String key : invKeys) {
                Long userNo = Long.parseLong(key.split(":")[3]);
                syncInventoryFromRedis(userNo);
            }
        }

        Set<String> displayKeys = redisTemplate.keys("delta:display:*");
        if (displayKeys != null) {
            for (String key : displayKeys) {
                Long userNo = Long.parseLong(key.split(":")[2]);
                syncDisplayFromRedis(userNo);
            }
        }
    }

    private void syncInventoryFromRedis(Long userNo) {
        String invDeltaKey = deltaInvFoodKey(userNo);
        Map<Object, Object> invDeltas = redisTemplate.opsForHash().entries(invDeltaKey);

        if (invDeltas == null || invDeltas.isEmpty()) {
            return;
        }

        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        for (Map.Entry<Object, Object> entry : invDeltas.entrySet()) {
            Long foodNo = Long.parseLong(entry.getKey().toString());

            Object currentObj = redisTemplate.opsForHash().get(invFoodKey(userNo), String.valueOf(foodNo));
            int currentCount = currentObj == null ? 0 : Integer.parseInt(currentObj.toString());

            UserInventory inventory = userInventoryRepository
                    .findByUserAndFoodOrIngredientNoAndItemType(user, foodNo, ItemType.FOOD)
                    .orElse(null);

            if (inventory == null) {
                if (currentCount > 0) {
                    inventory = UserInventory.builder()
                            .user(user)
                            .foodOrIngredientNo(foodNo)
                            .itemType(ItemType.FOOD)
                            .count(currentCount)
                            .build();
                    userInventoryRepository.save(inventory);
                }
            } else {
                inventory.updateCount(currentCount);
                userInventoryRepository.save(inventory);
            }
        }

        redisTemplate.delete(invDeltaKey);
    }

    private void syncDisplayFromRedis(Long userNo) {
        String displayDeltaKey = deltaDisplayKey(userNo);
        Map<Object, Object> displayDeltas = redisTemplate.opsForHash().entries(displayDeltaKey);

        if (displayDeltas == null || displayDeltas.isEmpty()) {
            return;
        }

        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        for (Map.Entry<Object, Object> entry : displayDeltas.entrySet()) {
            String field = entry.getKey().toString(); // tableNo:slotNo
            String[] parts = field.split(":");
            Integer tableNo = Integer.parseInt(parts[0]);
            Integer slotNo = Integer.parseInt(parts[1]);

            Object currentObj = redisTemplate.opsForHash().get(displaySlotKey(userNo), field);

            StoreDisplay display = storeDisplayRepository
                    .findByUserAndTableNoAndSlotNo(user, tableNo, slotNo)
                    .orElse(null);

            if (currentObj == null) {
                if (display != null) {
                    storeDisplayRepository.delete(display);
                }
                continue;
            }

            String[] raw = currentObj.toString().split(",");
            Long foodNo = Long.parseLong(raw[0]);
            Integer count = Integer.parseInt(raw[1]);

            if (display == null) {
                display = StoreDisplay.builder()
                        .user(user)
                        .tableNo(tableNo)
                        .slotNo(slotNo)
                        .foodNo(foodNo)
                        .count(count)
                        .build();
            } else {
                display.changeFood(foodNo, count);
            }

            storeDisplayRepository.save(display);
        }

        redisTemplate.delete(displayDeltaKey);
    }
}
