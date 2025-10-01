package com.server.esgcafe.service;

import com.server.esgcafe.domain.dto.npc.NpcCollectionBook;
import com.server.esgcafe.domain.entity.*;
import com.server.esgcafe.repository.NpcPreferredFoodRepository;
import com.server.esgcafe.repository.UserNpcProgressRepository;
import com.server.esgcafe.repository.UserRepository;
import com.server.esgcafe.repository.UserUnlockedRecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NpcCollectionService {

    private static final String LOCKED_PLACEHOLDER = "???";

    private final UserRepository userRepository;
    private final UserNpcProgressRepository userNpcProgressRepository;
    private final NpcPreferredFoodRepository npcPreferredFoodRepository;
    private final UserUnlockedRecipeRepository userUnlockedRecipeRepository;

    @Transactional(readOnly = true)
    public List<NpcCollectionBook> getCollectionByNickname(String nickname) {

        log.info("🍞npc collection book start");

        User user = userRepository.findByNickName(nickname)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND: " + nickname));

        // 1) 유저의 "해금된" NPC 진행도만 조회
        List<UserNpcProgress> progresses = userNpcProgressRepository.findByUserAndUnlockedTrue(user);
        if (progresses.isEmpty()) return List.of();

        // 2) 대상 NPC들
        List<Npc> npcs = progresses.stream()
                .map(UserNpcProgress::getNpc)
                .toList();

        // 3) NPC별 선호 빵(정의) 조회 + 그룹핑 (정렬 포함)
        List<NpcPreferredFood> npfList =
                npcPreferredFoodRepository.findByNpcInOrderByNpcAscSortOrderAsc(npcs);

        Map<Long, List<Food>> npcToPreferredFoods = new HashMap<>();
        for (NpcPreferredFood npf : npfList) {
            npcToPreferredFoods
                    .computeIfAbsent(npf.getNpc().getNpcNo(), k -> new ArrayList<>())
                    .add(npf.getFood());
        }

        // 4) 유저가 해금한 레시피
        List<Food> allPreferredFoods = npfList.stream()
                .map(NpcPreferredFood::getFood)
                .distinct()
                .toList();

        Set<Long> unlockedFoodIds = userUnlockedRecipeRepository
                .findByUserAndFoodIn(user, allPreferredFoods)
                .stream()
                .map(UserUnlockedRecipe::getFood)
                .map(Food::getFoodNo)
                .collect(java.util.stream.Collectors.toSet());

        // 5) DTO 매핑
        List<NpcCollectionBook> result = new ArrayList<>(progresses.size());
        for (UserNpcProgress prog : progresses) {
            Npc npc = prog.getNpc();
            List<Food> preferredFoods = npcToPreferredFoods.getOrDefault(npc.getNpcNo(), List.of());

            // NPC의 선호빵 개수/순서를 유지하면서, 미해금 레시피는 "???"
            List<String> preferredBreadsMasked = preferredFoods.stream()
                    .map(food -> unlockedFoodIds.contains(food.getFoodNo())
                            ? food.getName()
                            : LOCKED_PLACEHOLDER)
                    .toList();

            boolean anyVisible = preferredBreadsMasked.stream()
                    .anyMatch(name -> !LOCKED_PLACEHOLDER.equals(name));

            result.add(new NpcCollectionBook(
                    npc.getName(),
                    npc.getIntro(),
                    preferredBreadsMasked,
                    anyVisible,
                    prog.getVisitCount(),
                    true // 여기까지 왔으면 무조건 해금된 NPC만
            ));
        }

        log.info("🍞result : {}", result);

        log.info("🍞npc collection book end");

        return result;
    }


}