package com.server.esgcafe.controller;

import com.server.esgcafe.domain.dto.user.GoogleLoginRequest;
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
    public Response<Map<String, String>> login(@RequestBody GoogleLoginRequest request) {

        Map<String, String> result = googleLoginService.processLoginToken(request);

       return Response.success(result);
    }
}