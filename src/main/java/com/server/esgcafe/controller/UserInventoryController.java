package com.server.esgcafe.controller;

import com.server.esgcafe.domain.dto.userInventory.UserInventoryResponse;
import com.server.esgcafe.domain.dto.userInventory.UserInventorySaveRequest;
import com.server.esgcafe.domain.dto.userInventory.UserInventorySaveResponse;
import com.server.esgcafe.exception.Response;
import com.server.esgcafe.service.UserInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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

    @PostMapping("/save")
    public Response<UserInventorySaveResponse> saveUserInventory(@RequestBody UserInventorySaveRequest request) {

        log.info("Save user inventory: {}", request);
        UserInventorySaveResponse response = userInventoryService.saveUserInventory(request);

        return Response.success(response);
    }

}
