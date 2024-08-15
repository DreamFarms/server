package com.server.esgcafe.controller;

import com.server.esgcafe.domain.dto.user.UserRequest;
import com.server.esgcafe.domain.dto.user.UserResponse;
import com.server.esgcafe.exception.Response;
import com.server.esgcafe.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/save")
    public Response<UserResponse> savedUser(@RequestBody @Valid UserRequest request) {

        UserResponse response = userService.processUser(request);
        return Response.success(response);
    }
}
