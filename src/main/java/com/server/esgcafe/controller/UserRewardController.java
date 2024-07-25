package com.server.esgcafe.controller;

import com.server.esgcafe.domain.dto.UserReward.UserRewardSaveRequest;
import com.server.esgcafe.domain.dto.UserReward.UserRewardSaveResponse;
import com.server.esgcafe.exception.Response;
import com.server.esgcafe.service.UserRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/reward")
@RequiredArgsConstructor
public class UserRewardController {

    private final UserRewardService userRewardService;

    @PostMapping("/save")
    public Response<UserRewardSaveResponse> saveUserReward(@RequestBody UserRewardSaveRequest request) {

        UserRewardSaveResponse response = userRewardService.userRewardSave(request);

        return Response.success(response);
    }
}
