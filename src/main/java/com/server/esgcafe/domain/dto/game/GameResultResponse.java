package com.server.esgcafe.domain.dto.game;

import com.server.esgcafe.domain.entity.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResultResponse {

    private boolean isSaved;
    private LocalDateTime saveTime;

    public GameResultResponse(Game game) {
        this.isSaved = true;
        this.saveTime = game.getCreatedAt();
    }

}
