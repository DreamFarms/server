package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.user.UserSaveRequest;
import com.server.esgcafe.domain.dto.user.UserSaveResponse;
import com.server.esgcafe.domain.dto.userInventory.BreadInfo;
import com.server.esgcafe.domain.dto.userInventory.UserInventoryInfoResponse;
import com.server.esgcafe.domain.entity.Food;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserInventory;
import com.server.esgcafe.domain.enum_class.ItemType;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.FoodRepository;
import com.server.esgcafe.repository.UserInventoryRepository;
import com.server.esgcafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final UserInventoryRepository userInventoryRepository;

    public UserSaveResponse processUser(UserSaveRequest request) {

        String nickname = request.getNickname().trim(); // 앞뒤 공백 제거
        nickname = nickname.replaceAll("[\\p{Zs}\\u200B]", "");  // ZWS를 빈 문자열로 대체

        log.info("🍞nickname : {}", nickname);
        log.info("🍞nickname length : {}", nickname.length());

        // 닉네임의 각 문자와 유니코드 값을 출력
        for (int i = 0; i < nickname.length(); i++) {
            char c = nickname.charAt(i);
            log.info("🍞char at {}: '{}', Unicode: {}", i, c, (int) c);
        }

        // 닉네임 길이 검증
        if (nickname.length() > 5) {
            String errorMessage = "닉네임은 5글자 이하여야 합니다.";
            log.error("🍞Validation failed: {}", errorMessage);
            throw new AppException(ErrorCode.INVALID_NICKNAME_LENGTH);
        }

        Optional<User> existingUser = userRepository.findByNickName(nickname);

        if (existingUser.isPresent()) {
            // 사용자가 이미 존재하는 경우 -> 로그인 처리
            log.info("🍞User '{}' found, logging in...", nickname);
            User user = existingUser.get();
            return UserSaveResponse.from(user, "Login successful");
        } else {
            // 사용자가 존재하지 않는 경우 -> 회원 가입 처리
            log.info("🍞User '{}' not found, creating new user...", nickname);
            User newUser = request.toEntity();
            User savedUser = userRepository.save(newUser);
            return UserSaveResponse.from(savedUser, "Signup successful");
        }
    }

    @Transactional(readOnly = true)
    public UserInventoryInfoResponse userBreadInfo(String nickname) {

        log.info("🍞nickname : {}", nickname);

        // 유저 조회
        User user = userRepository.findByNickName(nickname)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 유저의 빵 리스트 조회
        List<UserInventory> userInventories = userInventoryRepository.findByUserAndItemType(user, ItemType.FOOD);

        // 빵 리스트와 개수 추출
        List<BreadInfo> breadInfoList = userInventories.stream()
                .collect(Collectors.groupingBy(
                        UserInventory::getFoodOrIngredientNo,  // foodOrIngredientNo로 그룹화
                        Collectors.summingInt(UserInventory::getCount)  // 개수 합산
                ))
                .entrySet().stream()
                .map(entry -> {
                    // foodOrIngredientNo로 Food 이름을 조회
                    Long foodId = entry.getKey();
                    Food food = foodRepository.findById(foodId)
                            .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));
                    return new BreadInfo(food.getName(), entry.getValue());
                })
                .collect(Collectors.toList());


        // 빵 리스트 로그 출력
        for (BreadInfo breadInfo : breadInfoList) {
            log.info("🍞 Bread: {}, Count: {}", breadInfo.getName(), breadInfo.getCount());
        }

        // 응답 객체 생성
        return new UserInventoryInfoResponse(breadInfoList);
    }
}