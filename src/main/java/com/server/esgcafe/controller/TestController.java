package com.server.esgcafe.controller;

import com.server.esgcafe.domain.dto.test.TestCurrencyGrantRequest;
import com.server.esgcafe.domain.dto.test.TestCurrencyGrantResponse;
import com.server.esgcafe.exception.Response;
import com.server.esgcafe.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @PostMapping("/test/currency")
    public Response<TestCurrencyGrantResponse> grantTestCurrency( @RequestBody TestCurrencyGrantRequest request) {

        TestCurrencyGrantResponse response = testService.grantTestCurrency(request);
        return Response.success(response);

    }
}
