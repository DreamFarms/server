package com.server.esgcafe.domain.dto.game;

import com.server.esgcafe.domain.entity.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResultRequest {

    private String nickname;
    private int revenue;
    private int cash;
    private int totalVisitors;
    private List<SoldBreadRequest> soldBreads;

    public Game saveGame(Game game) {
        return Game.builder()
                .gameNo(game.getGameNo())
                .level(game.getLevel())
                .revenue(game.getRevenue() + this.revenue)
                .cash(game.getCash() + this.cash)
                .totalVisitors(game.getTotalVisitors() + this.totalVisitors)
                .build();
    }

}
