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
    INVALID_NICKNAME_LENGTH(HttpStatus.BAD_REQUEST, "닉네임은 5자 이하여야합니다."),
    MISSING_INGREDIENTS(HttpStatus.NOT_FOUND, "사용자가 제출한 재료 목록이 없습니다."),
    INVALID_INVENTORY_ITEM(HttpStatus.BAD_REQUEST, "인벤토리 저장 방식이 잘못되었습니다."),
    INGREDIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 재료를 찾을 수 없습니다."),
    INSUFFICIENT_INGREDIENTS(HttpStatus.BAD_REQUEST, "재료가 부족합니다."),
    INVALID_AUDIENCE(HttpStatus.UNAUTHORIZED, "ID 토큰의 대상(audience)이 유효하지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "ID 토큰이 만료되었습니다."),
    INVALID_ISSUER(HttpStatus.UNAUTHORIZED, "ID 토큰의 발급자(issuer)가 유효하지 않습니다."),
    ID_TOKEN_VERIFICATION_FAILED(HttpStatus.UNAUTHORIZED, "ID 토큰 검증에 실패했습니다."),
    INVALID_ID_TOKEN(HttpStatus.BAD_REQUEST, "ID 토큰이 비어있거나 유효하지 않습니다."),
    MALFORMED_ID_TOKEN(HttpStatus.BAD_REQUEST, "ID 토큰 형식이 잘못되었습니다.")


    ;

    private HttpStatus status;
    private String message;
}


