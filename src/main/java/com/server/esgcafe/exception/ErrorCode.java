package com.server.esgcafe.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB 에러"),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "이미 등록되어있는 닉네임 입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    FOOD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 빵이 존재하지 않습니다."),
    REWARD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 재료가 존재하지 않습니다."),
    NOT_ENOUGH_BREAD(HttpStatus.NOT_FOUND, "보유한 빵의 개수가 충분하지 않습니다."),
    GAME_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게임이 존재하지 않습니다."),
    ;

    private HttpStatus status;
    private String message;
}


