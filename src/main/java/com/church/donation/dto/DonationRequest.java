package com.church.donation.dto; // 본인의 패키지 경로에 맞게 수정하세요

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class DonationRequest {
    private String memberId;
    private Long amount;
    private String itemCode;
    private String donationDate; // "2026-05-13" 형태의 문자열로 받기
}