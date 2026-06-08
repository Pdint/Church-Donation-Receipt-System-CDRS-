package com.church.donation.dto;

import com.church.donation.domain.ChurchInfo;
import com.church.donation.domain.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class ReceiptResponse {

    // 화면(receipt.html)에서 쓰기 편하도록 객체를 통째로 담습니다.
    private Member member;
    private Long totalAmount;      // 헌금 총액 (Long 타입으로 통일)
    private ChurchInfo churchInfo; // 교회 정보

}