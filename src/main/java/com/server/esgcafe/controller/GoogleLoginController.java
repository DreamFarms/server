package com.server.esgcafe.controller;

import com.server.esgcafe.domain.dto.user.GoogleLoginRequest;
import com.server.esgcafe.domain.dto.user.GoogleLoginResponse;
import com.server.esgcafe.exception.Response;
import com.server.esgcafe.service.GoogleLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/google-login")
@RequiredArgsConstructor
public class GoogleLoginController {

    private final GoogleLoginService googleLoginService;

    @PostMapping("/login")
    public Response<GoogleLoginResponse> login(@RequestBody GoogleLoginRequest request) {

        GoogleLoginResponse response = googleLoginService.processLoginToken(request);

       return Response.success(response);
    }

    @PostMapping("/token/refresh")
    public Response<String> refresh(@RequestBody String refreshToken) {

        String newAccessToken = googleLoginService.refreshAccessToken(refreshToken);

        return Response.success(newAccessToken);
    }

//    @PostMapping("/decoding")
//    public Response<String> decoding(@RequestBody String idToken) {
//
//        String payload = googleLoginService.decodeIdToken(idToken);
//
//        return Response.success(payload);
//    }
}