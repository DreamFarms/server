package com.server.esgcafe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EconomyRedisService {

    private final RedisTemplate<String, String> redisTemplate;

    private final DefaultRedisScript<List> sellBreadScript;
    private final DefaultRedisScript<List> spendGoldScript;
    private final DefaultRedisScript<List> earnGoldScript;
    private final DefaultRedisScript<List> placeBreadScript;
    private final DefaultRedisScript<List> removeBreadScript;

    // 키 규칙
    private static String invFoodKey(Long userNo) { return "inv:food:" + userNo; }
    private static String goldKey(Long userNo) { return "wallet:gold:" + userNo; }
    private static String deltaGoldKey() { return "delta:gold"; }
    private static String deltaInvFoodKey(Long userNo) { return "delta:inv:food:" + userNo; }
    private static String displaySlotKey(Long userNo) { return "display:slot:" + userNo; }
    private static String deltaDisplayKey(Long userNo) { return "delta:display:" + userNo; }

    /**
     * 매장 판매(빵) - 원자 처리
     * @return result[0] = 1 성공/0 실패
     *         result[1] = 남은 재고(또는 현재 재고)
     *         result[2] = 증가 골드
     */
    // qty : 몇 개 팔렸는지, goldInc : 얼마 벌었는지(price*qty)
    public List<?> sellBread(Long userNo, Integer tableNo, Integer slotNo, Long expectedFoodNo, int quantity, long goldInc) {
        List<String> keys = List.of(
                displaySlotKey(userNo),
                goldKey(userNo),
                deltaGoldKey(),
                deltaDisplayKey(userNo)
        );

        return redisTemplate.execute(
                sellBreadScript,
                keys,
                String.valueOf(userNo),
                String.valueOf(tableNo),
                String.valueOf(slotNo),
                String.valueOf(expectedFoodNo),
                String.valueOf(quantity),
                String.valueOf(goldInc)
        );
    }

    public List<?> placeBread(Long userNo, Integer tableNo, Integer slotNo, Long foodNo, int quantity) {
        List<String> keys = List.of(
                invFoodKey(userNo),
                displaySlotKey(userNo),
                deltaInvFoodKey(userNo),
                deltaDisplayKey(userNo)
        );

        return redisTemplate.execute(
                placeBreadScript,
                keys,
                String.valueOf(tableNo),
                String.valueOf(slotNo),
                String.valueOf(foodNo),
                String.valueOf(quantity)
        );
    }

    public List<?> removeBread(Long userNo, Integer tableNo, Integer slotNo, int quantity) {
        List<String> keys = List.of(
                displaySlotKey(userNo),
                invFoodKey(userNo),
                deltaDisplayKey(userNo),
                deltaInvFoodKey(userNo)
        );

        return redisTemplate.execute(
                removeBreadScript,
                keys,
                String.valueOf(tableNo),
                String.valueOf(slotNo),
                String.valueOf(quantity)
        );
    }


    public String getDisplaySlotRaw(Long userNo, Integer tableNo, Integer slotNo) {
        Object raw = redisTemplate.opsForHash().get(
                displaySlotKey(userNo),
                tableNo + ":" + slotNo
        );
        return raw == null ? null : raw.toString();
    }

    /**
     * 골드 차감(상점/미니게임/강화 공통)
     * @return result[0] = 1 성공 / 0 실패
     *         result[1] = 남은 골드(또는 현재 골드)
     */
    public List<?> spendGold(Long userNo, long cost) {
        List<String> keys = List.of(
                goldKey(userNo),
                deltaGoldKey()
        );

        return redisTemplate.execute(
                spendGoldScript,
                keys,
                String.valueOf(userNo),
                String.valueOf(cost)
        );
    }

}
