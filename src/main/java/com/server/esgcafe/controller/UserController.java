package com.server.esgcafe.controller;

import com.server.esgcafe.domain.dto.user.UserSaveRequest;
import com.server.esgcafe.domain.dto.user.UserSaveResponse;
import com.server.esgcafe.domain.dto.userBread.UserBreadInfoResponse;
import com.server.esgcafe.exception.Response;
import com.server.esgcafe.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/save")
    public Response<UserSaveResponse> savedUser(@RequestBody @Valid UserSaveRequest request) {

        UserSaveResponse response = userService.processUser(request);
        return Response.success(response);
    }

    @GetMapping("/bread-info")
    public Response<UserBreadInfoResponse> getBreadInfo(@RequestParam String nickname) {

        UserBreadInfoResponse response = userService.userBreadInfo(nickname);
        return Response.success(response);
    }
}
