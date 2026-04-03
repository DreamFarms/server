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

    // 키 규칙
    private static String invFoodKey(Long userNo) { return "inv:food:" + userNo; }
    private static String goldKey(Long userNo) { return "wallet:gold:" + userNo; }
    private static String deltaGoldKey() { return "delta:gold"; }
    private static String deltaInvFoodKey(Long userNo) { return "delta:inv:food:" + userNo ; }

    /**
     * 매장 판매(빵) - 원자 처리
     * @return result[0] = 1 성공/0 실패
     *         result[1] = 남은 재고(또는 현재 재고)
     *         result[2] = 증가 골드
     */
    // qty : 몇 개 팔렸는지, goldInc : 얼마 벌었는지(price*qty)
    public List<?> sellBread(Long userNo, Long foodNo, int qty, long goldInc) {
        List<String> keys = List.of(
                invFoodKey(userNo),
                goldKey(userNo),
                deltaGoldKey(),
                deltaInvFoodKey(userNo)
        );

        return redisTemplate.execute(
                sellBreadScript,
                keys,
                String.valueOf(userNo),
                String.valueOf(foodNo),
                String.valueOf(qty),
                String.valueOf(goldInc)
        );
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
