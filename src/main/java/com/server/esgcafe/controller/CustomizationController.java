package com.server.esgcafe.controller;

import com.server.esgcafe.domain.dto.customization.CustomizationResponse;
import com.server.esgcafe.domain.dto.customization.CustomizationUpdateRequest;
import com.server.esgcafe.exception.Response;
import com.server.esgcafe.service.CustomizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customizations")
@RequiredArgsConstructor
public class CustomizationController {

    private final CustomizationService customizationService;

    @GetMapping
    public Response<CustomizationResponse> getCustomizations(@RequestParam String nickname) {

        CustomizationResponse response = customizationService.getCustomizations(nickname);
        return Response.success(response);
    }

    @PostMapping("/update")
    public Response<CustomizationResponse> updateCustomizations(@RequestBody CustomizationUpdateRequest request) {

        CustomizationResponse response = customizationService.updateCustomizations(request);
        return Response.success(response);
    }
}
