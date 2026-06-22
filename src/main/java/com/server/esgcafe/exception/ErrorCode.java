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
    INVALID_NICKNAME_LENGTH(HttpStatus.BAD_REQUEST, "닉네임은 8자 이하여야합니다."),
    MISSING_INGREDIENTS(HttpStatus.NOT_FOUND, "사용자가 제출한 재료 목록이 없습니다."),
    INVALID_INVENTORY_ITEM(HttpStatus.BAD_REQUEST, "인벤토리 저장 방식이 잘못되었습니다."),
    INGREDIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 재료를 찾을 수 없습니다."),
    INSUFFICIENT_INGREDIENTS(HttpStatus.BAD_REQUEST, "재료가 부족합니다."),
    INVALID_AUDIENCE(HttpStatus.UNAUTHORIZED, "ID 토큰의 대상(audience)이 유효하지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "ID 토큰이 만료되었습니다."),
    INVALID_ISSUER(HttpStatus.UNAUTHORIZED, "ID 토큰의 발급자(issuer)가 유효하지 않습니다."),
    ID_TOKEN_VERIFICATION_FAILED(HttpStatus.UNAUTHORIZED, "ID 토큰 검증에 실패했습니다."),
    INVALID_ID_TOKEN(HttpStatus.BAD_REQUEST, "ID 토큰이 비어있거나 유효하지 않습니다."),
    MALFORMED_ID_TOKEN(HttpStatus.BAD_REQUEST, "ID 토큰 형식이 잘못되었습니다."),
    TOKEN_EXCHANGE_FAILED(HttpStatus.BAD_REQUEST, "토큰 발급에 실패했습니다."),
    MISSING_GOOGLE_CLIENT_ID(HttpStatus.NOT_FOUND, "Google Client ID가 필요합니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "Refresh Token이 만료되었습니다."),
    QUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 Quest가 존재하지 않습니다."),
    ACCESSTOKEN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    SHOP_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상점 상품을 찾을 수 없습니다."),
    SHOP_PRODUCT_INACTIVE(HttpStatus.BAD_REQUEST, "현재 판매 중인 상품이 아닙니다."),
    INVALID_SHOP_PRODUCT(HttpStatus.BAD_REQUEST, "구매할 수 없는 상품입니다."),
    REAL_MONEY_PRODUCT(HttpStatus.BAD_REQUEST, "실제 결제 상품은 Google Play 검증 API를 사용해야 합니다."),
    INSUFFICIENT_CASH(HttpStatus.BAD_REQUEST, "캐시가 부족합니다."),
    INSUFFICIENT_GOLD(HttpStatus.BAD_REQUEST, "골드가 부족합니다."),
    INVALID_PRODUCT_CODE(HttpStatus.BAD_REQUEST, "상품 코드가 올바르지 않습니다."),
    NOT_ENOUGH_TICKET(HttpStatus.BAD_REQUEST, "티켓이 부족합니다."),
    SHOP_REWARD_NOT_FOUND(HttpStatus.BAD_REQUEST, "상점 상품 보상 정보가 없습니다."),
    INVALID_SHOP_REWARD(HttpStatus.BAD_REQUEST, "올바르지 않은 상점 보상 정보입니다.")


    ;

    private HttpStatus status;
    private String message;
}


