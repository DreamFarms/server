package com.server.esgcafe.controller;

import com.server.esgcafe.domain.dto.userInventory.UserInventoryResponse;
import com.server.esgcafe.exception.Response;
import com.server.esgcafe.service.UserInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class UserInventoryController {

    private final UserInventoryService userInventoryService;

    @GetMapping("/load")
    public Response<List<UserInventoryResponse>> getUserInventory(@RequestParam String nickname) {

        List<UserInventoryResponse> response = userInventoryService.getUserInventory(nickname);

        return Response.success(response);
    }

}
