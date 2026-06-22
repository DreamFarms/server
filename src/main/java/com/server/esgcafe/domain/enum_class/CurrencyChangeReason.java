package com.server.esgcafe.domain.enum_class;

public enum CurrencyChangeReason {

    CASH_PURCHASE,       // 실제 결제로 cash 증가
    GOLD_PURCHASE,       // cash로 gold 구매
    ITEM_PURCHASE,       // gold/cash로 아이템 구매
    ATTENDANCE_REWARD,   // 출석으로 cash/gold 지급
    EVENT_REWARD,        // 이벤트로 cash/gold 지급
    REFUND_ROLLBACK,     // 환불로 재화 회수
    ADMIN_GRANT          // 운영자 수동 지급

}
