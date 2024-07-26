package com.server.esgcafe.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB 에러"),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "이미 등록되어있는 닉네임 입니다."),
    USER_NOTFOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),


    ;

    private HttpStatus status;
    private String message;
}


