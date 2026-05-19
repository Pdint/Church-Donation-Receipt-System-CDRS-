package com.church.donation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReceiptResponse {
    // 기부자 정보
    private String memberId;
    private String name;
    private String birthDate;

    // 기부 내역 (표에 들어갈 내용)
    private String itemCode;     // "41" 고정
    private String itemName;     // "각종 헌금" 고정
    private String dateRange;    // 예: "2026.01 - 2026.12"
    private Long totalAmount;    // 1년간 총 헌금액 합산
}