package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.customization.CustomizationChangeRequest;
import com.server.esgcafe.domain.dto.customization.CustomizationResponse;
import com.server.esgcafe.domain.dto.customization.CustomizationThemeInfo;
import com.server.esgcafe.domain.dto.customization.CustomizationUpdateRequest;
import com.server.esgcafe.domain.entity.User;
import com.server.esgcafe.domain.entity.UserCustomization;
import com.server.esgcafe.domain.enum_class.FurnitureType;
import com.server.esgcafe.domain.enum_class.ThemeCode;
import com.server.esgcafe.exception.AppException;
import com.server.esgcafe.exception.ErrorCode;
import com.server.esgcafe.repository.UserCustomizationRepository;
import com.server.esgcafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomizationService {

    private final UserRepository userRepository;
    private final UserCustomizationRepository userCustomizationRepository;

    @Transactional
    public void initializeDefaultCustomizations(User user) {

        EnumSet<FurnitureType> savedThemeParts = userCustomizationRepository.findByUser(user).stream()
                .map(UserCustomization::getThemePartType)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(FurnitureType.class)));

        List<UserCustomization> defaultCustomizations = Arrays.stream(FurnitureType.values())
                .filter(themePartType -> !savedThemeParts.contains(themePartType))
                .map(themePartType -> UserCustomization.builder()
                        .user(user)
                        .themePartType(themePartType)
                        .themeCode(ThemeCode.DEFAULT)
                        .build())
                .toList();

        if (defaultCustomizations.isEmpty()) {
            log.info("[CUSTOMIZATION] 기본값 초기화 생략 - 모든 설정 존재, userNo: {}", user.getUserNo());
            return;
        }

        userCustomizationRepository.saveAll(defaultCustomizations);

        log.info("[CUSTOMIZATION] 신규 유저 기본값 저장 완료 - userNo: {}, nickname: {}, count: {}",
                user.getUserNo(),
                user.getNickName(),
                defaultCustomizations.size());
    }

    @Transactional(readOnly = true)
    public CustomizationResponse getCustomizations(String nickname) {

        log.info("[CUSTOMIZATION] 조회 시작 - nickname: {}", nickname);

        User user = userRepository.findByNickName(nickname)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        CustomizationResponse response = createResponse(user);

        log.info("[CUSTOMIZATION] 조회 완료 - nickname: {}, themes: {}",
                nickname,
                response.getThemes());

        return response;
    }

    private void validateChange(
            CustomizationChangeRequest change,
            EnumSet<FurnitureType> requestedThemeParts) {

        if (change == null || change.getThemePartType() == null || change.getThemeCode() == null) {
            log.warn("[CUSTOMIZATION] 올바르지 않은 변경 항목 - change: {}", change);
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        if (!requestedThemeParts.add(change.getThemePartType())) {
            log.warn("[CUSTOMIZATION] 중복된 파트 변경 요청 - themePartType: {}", change.getThemePartType());
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
    }

    @Transactional
    public CustomizationResponse updateCustomizations(CustomizationUpdateRequest request) {

        log.info("[CUSTOMIZATION] 변경 시작 - request: {}", request);

        if (request == null
                || request.getNickname() == null
                || request.getNickname().isBlank()
                || request.getChanges() == null
                || request.getChanges().isEmpty()) {
            log.warn("[CUSTOMIZATION] 올바르지 않은 변경 요청 - request: {}", request);
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        EnumSet<FurnitureType> requestedThemeParts = EnumSet.noneOf(FurnitureType.class);
        request.getChanges().forEach(change -> validateChange(change, requestedThemeParts));

        User user = userRepository.findByNickName(request.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Map<FurnitureType, UserCustomization> savedCustomizations = userCustomizationRepository
                .findByUser(user)
                .stream()
                .collect(Collectors.toMap(
                        UserCustomization::getThemePartType,
                        customization -> customization
                ));

        List<UserCustomization> changedCustomizations = new ArrayList<>();

        for (CustomizationChangeRequest change : request.getChanges()) {
            UserCustomization customization = savedCustomizations.get(change.getThemePartType());

            if (customization == null) {
                customization = UserCustomization.builder()
                        .user(user)
                        .themePartType(change.getThemePartType())
                        .themeCode(change.getThemeCode())
                        .build();

                changedCustomizations.add(customization);

                log.info("[CUSTOMIZATION] 신규 저장 - nickname: {}, themePartType: {}, themeCode: {}",
                        request.getNickname(),
                        change.getThemePartType(),
                        change.getThemeCode());
                continue;
            }

            ThemeCode beforeThemeCode = customization.getThemeCode();
            if (beforeThemeCode == change.getThemeCode()) {
                log.info("[CUSTOMIZATION] 변경 생략 - 동일한 설정, nickname: {}, themePartType: {}, themeCode: {}",
                        request.getNickname(),
                        change.getThemePartType(),
                        change.getThemeCode());
                continue;
            }

            customization.updateThemeCode(change.getThemeCode());
            changedCustomizations.add(customization);

            log.info("[CUSTOMIZATION] 변경 완료 - nickname: {}, themePartType: {}, before: {}, after: {}",
                    request.getNickname(),
                    change.getThemePartType(),
                    beforeThemeCode,
                    change.getThemeCode());
        }

        userCustomizationRepository.saveAll(changedCustomizations);
        CustomizationResponse response = createResponse(user);

        log.info("[CUSTOMIZATION] 변경 요청 완료 - nickname: {}, changeCount: {}",
                request.getNickname(),
                request.getChanges().size());

        return response;
    }

    private CustomizationResponse createResponse(User user) {

        Map<FurnitureType, ThemeCode> savedThemes = userCustomizationRepository.findByUser(user).stream()
                .collect(Collectors.toMap(
                        UserCustomization::getThemePartType,
                        UserCustomization::getThemeCode
                ));

        List<CustomizationThemeInfo> themes = Arrays.stream(FurnitureType.values())
                .map(themePartType -> new CustomizationThemeInfo(
                        themePartType,
                        savedThemes.getOrDefault(themePartType, ThemeCode.DEFAULT)
                ))
                .toList();

        return new CustomizationResponse(user.getNickName(), themes);
    }
}
