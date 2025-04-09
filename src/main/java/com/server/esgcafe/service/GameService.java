package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.game.*;
import com.server.esgcafe.domain.entity.Food;
import com.server.esgcafe.domain.entity.Game;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserInventory;
import com.server.esgcafe.domain.enum_class.ItemType;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.FoodRepository;
import com.server.esgcafe.repository.GameRepository;
import com.server.esgcafe.repository.UserInventoryRepository;
import com.server.esgcafe.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameService {

    private final RedisTemplate<String, String> redisTemplate;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final UserInventoryRepository userInventoryRepository;

    private static final String SALES_QUEUE = "bread:sales:queue";

    // 빵 판매 요청 → Redis Queue 저장
    @Async
    public CompletableFuture<Void> sellBreadAsync(BreadSaleRequest request) {

        log.info("request nickname : {}", request.getNickname());
        log.info("request breadItems : {}", request.getBreadList());

        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        for (BreadSaleItem item : request.getBreadList()) {
            Food food = foodRepository.findByName(item.getName())
                    .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

            UserInventory userInventory = userInventoryRepository
                    .findByUserAndFoodOrIngredientNoAndItemType(user, food.getFoodNo(), ItemType.FOOD)
                    .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

            // 빵 보유 개수 확인
            if (userInventory.getCount() < item.getQuantity()) {
                throw new AppException(ErrorCode.NOT_ENOUGH_BREAD);
            }

            // Redis에 판매 정보 저장
            String saleData = user.getUserNo() + "," + food.getFoodNo() + "," + item.getQuantity();  //request.getNpcId() + "," +
            redisTemplate.opsForList().leftPush(SALES_QUEUE, saleData);

            System.out.println("✅ [DEBUG] Redis에 데이터 저장됨: " + saleData);
        }
        return CompletableFuture.completedFuture(null);
    }

    // 5초마다 Redis Queue에서 판매 데이터 가져와 DB 반영
    @Scheduled(fixedRate = 5000)
    @Async
    public CompletableFuture<Void> processBreadSalesAsync() {

//        System.out.println("🔥 [DEBUG] 빵 판매 배치 실행됨: " + LocalDateTime.now());

        while (true) {
            String saleData = redisTemplate.opsForList().rightPop(SALES_QUEUE);
            if (saleData == null) {
//                System.out.println("📌 [DEBUG] Redis Queue가 비어 있음");
                break;
            }

            String[] data = saleData.split(",");
            Long userNo = Long.parseLong(data[0]); // 로그인한 유저 ID
//            Long npcId = Long.parseLong(data[1]);  // NPC ID
            Long foodNo = Long.parseLong(data[1]); // 판매된 빵 ID
            int quantity = Integer.parseInt(data[2]); // 판매 개수

            System.out.println("✅ [DEBUG] Redis에서 데이터 꺼냄: " + Arrays.toString(data));

            userRepository.findById(userNo).ifPresent(user -> {
                foodRepository.findById(foodNo).ifPresent(food -> {
                    userInventoryRepository.findByUserAndFoodOrIngredientNoAndItemType(user, food.getFoodNo(), ItemType.FOOD)
                            .ifPresentOrElse(inventory -> {
                                if (inventory.getCount() >= quantity) {
                                    // 빵 개수 차감
                                    inventory.subtractInventoryCount(quantity);

                                    // 유저 골드 증가
                                    long goldIncrease = (long) food.getPrice() * quantity;
                                    user.addGold(goldIncrease);

                                    System.out.println("✅ [DEBUG] 유저 골드 증가: +" + goldIncrease + " 현재 골드: " + user.getGold());

                                    // 변경 사항 저장
                                    userInventoryRepository.save(inventory);
                                    userRepository.save(user);

                                    System.out.println("✅ NPC가 " + food.getName() + " " + quantity + "개를 구매함.");
                                } else {
                                    System.out.println("🚨 유저 인벤토리에 빵이 부족함: " + food.getName());
                                }
                            }, () -> System.out.println("🚨 해당 유저가 " + food.getName() + "을 보유하지 않음."));
                });
            });
        }
        return CompletableFuture.completedFuture(null);
    }


    @Transactional
    public GameResultResponse saveGameResult(GameResultRequest request) {

        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Game game = gameRepository.findByUser(user)
                .orElseThrow(() -> new AppException(ErrorCode.GAME_NOT_FOUND));

        Game savedGame = request.saveGame(game);
        gameRepository.save(savedGame);

        // 판매된 빵 처리
        for (SoldBreadRequest soldBread : request.getSoldBreads()) {
            Food food = soldBread.getFood(foodRepository);

            // UserInventory에서 Food 아이템을 조회
            UserInventory userInventory = userInventoryRepository.findByUserAndFoodOrIngredientNoAndItemType(user, food.getFoodNo(), ItemType.FOOD)
                    .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

            // 빵 개수 차감
            userInventory.subtractInventoryCount(soldBread.getSoldCount());
            userInventoryRepository.save(userInventory);
        }

        return new GameResultResponse(savedGame);
    }

}
