package com.server.esgcafe.service;

import com.server.esgcafe.configuration.jwt.JwtProvider;
import com.server.esgcafe.domain.dto.user.UserSaveRequest;
import com.server.esgcafe.domain.dto.user.UserSaveResponse;
import com.server.esgcafe.domain.dto.user.UserSimpleInfoResponse;
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
import io.jsonwebtoken.Claims;
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
    private final JwtProvider jwtProvider;

    public UserSaveResponse processUser(UserSaveRequest request, String accessToken) {

        String token = accessToken.replace("Bearer ", "");
        log.info("✅ 받은 accessToken: {}", token);

        String googleId;
        try {
            Claims claims = jwtProvider.parseClaims(token);
            googleId = claims.getSubject();
            log.info("✅ 토큰 인증 성공: {}", googleId);
        } catch (Exception e) {
            log.error("❌ 토큰 파싱 실패: {}", e.getMessage());
            throw new AppException(ErrorCode.ACCESSTOKEN_UNAUTHORIZED, "토큰이 유효하지 않습니다.");
        }

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
        if (nickname.length() > 8) {
            String errorMessage = "닉네임은 8글자 이하여야 합니다.";
            log.error("🍞Validation failed: {}", errorMessage);
            throw new AppException(ErrorCode.INVALID_NICKNAME_LENGTH);
        }

        // 금지어 필터링 추가 예정


        // 1. accessToken으로 유저 인증 정보 추출
        googleId = jwtProvider.parseClaims(accessToken.replace("Bearer ", "")).getSubject();
        User user = userRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 2. 중복 닉네임 검증 (자기 자신 제외)
        Optional<User> duplicate = userRepository.findByNickName(nickname);
        if (duplicate.isPresent() && !duplicate.get().getGoogleId().equals(user.getGoogleId())) {
            throw new AppException(ErrorCode.DUPLICATED_NICKNAME);
        }

        // 3. 닉네임 저장
        user.updateNickName(nickname);
        userRepository.save(user);

        return UserSaveResponse.from(user, "닉네임 설정이 완료되었습니다.");

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


    public UserSimpleInfoResponse getUserSimpleInfo(Long userNo) {

        log.info("user No : {}", userNo);

        User user = userRepository.findByUserNo(userNo)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        UserSimpleInfoResponse response = new UserSimpleInfoResponse(user.getNickName(), user.getCash(), user.getGold());

        log.info("UserSimpleInfoResponse: {}", response);

        return response;
    }
}