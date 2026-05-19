package com.church.donation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Donation extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberId;
    private Long amount;
    private LocalDate donationDate; // 실제 기부 날짜
    private LocalDateTime createdAt; // 시스템 등록 시각

    public Donation(String memberId, Long amount,String donationDate) {
        this.memberId = memberId;
        this.amount = amount;
        // 문자열로 들어온 날짜를 LocalDate 객체로 변환해서 저장
        this.donationDate = LocalDate.parse(donationDate);
        this.createdAt = LocalDateTime.now();
    }
}