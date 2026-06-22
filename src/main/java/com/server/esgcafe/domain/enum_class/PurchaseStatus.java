package com.server.esgcafe.domain.enum_class;

public enum PurchaseStatus {

    REQUESTED,          // 구매 요청 생성
    PENDING,            // Google 결제 대기
    PURCHASED,          // Google 구매 완료 확인
    VERIFY_FAILED,      // Google 검증 실패
    REWARD_GRANTED,     // 보상 지급 완료
    ALREADY_PROCESSED,  // 이미 처리된 purchaseToken
    ACKNOWLEDGED,       // acknowledge 완료
    CONSUMED,           // consume 완료
    CANCELED,           // 취소
    REFUNDED,           // 환불
    FAILED              // 기타 실패

}
