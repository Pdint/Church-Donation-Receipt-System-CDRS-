package com.church.donation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class DonationRequest {
    private String memberId;      // 👈 이름, 생일, 전화번호 3개가 ID 딱 1개로 압축됨!
    private Long amount;
    private String donationDate;
}