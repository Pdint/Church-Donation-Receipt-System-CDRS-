package com.church.donation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class DonationRequest {
    private String name;          // 관리자가 화면에서 입력한 교인 성함
    private String birthDate;     // 관리자가 화면에서 입력한 교인 생년월일(6자리)
    private Long amount;          // 기부(헌금) 금액
    private String donationDate;  // 정산 날짜 (예: "2026-05-19")
}