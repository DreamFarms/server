package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.game.GameResultRequest;
import com.server.esgcafe.domain.dto.game.GameResultResponse;
import com.server.esgcafe.domain.dto.game.SoldBreadRequest;
import com.server.esgcafe.domain.entity.*;
import com.server.esgcafe.domain.enum_class.ItemType;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final UserInventoryRepository userInventoryRepository;


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
