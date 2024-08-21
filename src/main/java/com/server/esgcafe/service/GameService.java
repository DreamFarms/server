package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.game.GameResultRequest;
import com.server.esgcafe.domain.dto.game.GameResultResponse;
import com.server.esgcafe.domain.dto.game.SoldBreadRequest;
import com.server.esgcafe.domain.entity.Food;
import com.server.esgcafe.domain.entity.Game;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserBread;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.FoodRepository;
import com.server.esgcafe.repository.GameRepository;
import com.server.esgcafe.repository.UserBreadRepository;
import com.server.esgcafe.repository.UserRepository;
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
    private final UserBreadRepository userBreadRepository;


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
            UserBread userBread = userBreadRepository.findByUserAndFood(user, food)
                    .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

            userBread.subtractBreadCount(soldBread.getSoldCount());  // 빵 개수 차감
            userBreadRepository.save(userBread);
        }

        return new GameResultResponse(savedGame);
    }




}
