package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.userInventory.UserInventoryResponse;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserInventory;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.FoodRepository;
import com.server.esgcafe.repository.UserInventoryRepository;
import com.server.esgcafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserInventoryService {

    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final UserInventoryRepository userInventoryRepository;


    public List<UserInventoryResponse> getUserInventory(String nickname) {

        User user = userRepository.findByNickName(nickname)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<UserInventory> userInventories = userInventoryRepository.findByUser(user);

        // UserInventory 엔티티를 UserInventoryResponse로 변환
        List<UserInventoryResponse> response = new ArrayList<>();
        for (UserInventory inventory : userInventories) {
            int code = inventory.getFood() != null ? inventory.getFood().getCode() : inventory.getIngredient().getCode();
            String name = inventory.getFood() != null ? inventory.getFood().getName() : inventory.getIngredient().getName();
            int count = inventory.getCount();

            response.add(new UserInventoryResponse(code, name, count));
        }

        return response;
    }
}
