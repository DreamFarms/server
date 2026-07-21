package com.server.esgcafe.controller;

import com.server.esgcafe.domain.dto.user.UserSaveRequest;
import com.server.esgcafe.domain.dto.user.UserSaveResponse;
import com.server.esgcafe.domain.dto.user.UserSaveTestResponse;
import com.server.esgcafe.domain.dto.user.UserSimpleInfoResponse;
import com.server.esgcafe.domain.dto.userInventory.UserInventoryInfoResponse;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.exception.Response;
import com.server.esgcafe.service.UserService;
import com.server.esgcafe.service.UserTestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserTestService userTestService;

    @PostMapping("/nickname/save")
    public Response<UserSaveResponse> savedUser(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody @Valid UserSaveRequest request) {

        if (authorizationHeader == null) {
            log.warn("❗ Authorization 헤더가 없습니다.");
            throw new AppException(ErrorCode.ACCESSTOKEN_UNAUTHORIZED, "Authorization 헤더가 없습니다.");
        }

        String accessToken = authorizationHeader.replace("Bearer ", "");
        log.info("✅ Authorization 헤더: {}", authorizationHeader);

        UserSaveResponse response = userService.processUser(request, accessToken);
        return Response.success(response);
    }

    @GetMapping("/bread-info")
    public Response<UserInventoryInfoResponse> getBreadInfo(@RequestParam String nickname) {

        UserInventoryInfoResponse response = userService.userBreadInfo(nickname);
        return Response.success(response);
    }

    @GetMapping("/{userNo}/info")
    public Response<UserSimpleInfoResponse> getUserSimpleInfo(@PathVariable Long userNo) {
        UserSimpleInfoResponse response = userService.getUserSimpleInfo(userNo);
        return Response.success(response);
    }

    @PostMapping("/nickname/login")
    public Response<UserSaveTestResponse> savedUser( @RequestBody @Valid UserSaveRequest request) {

        log.info("임시 닉네임 회원가입/로그인 요청 - nickname: {}", request.getNickname());

        UserSaveTestResponse response = userTestService.processTempUserByNickname(request);

        return Response.success(response);
    }
}
