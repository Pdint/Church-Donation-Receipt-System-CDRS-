package com.church.donation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;          // 기부자 성함 (영수증용)
    private String memberId;      // 교인 ID (마스터 테이블 연결용)
    private Long amount;          // 기부 금액
    private String phone;         // 전화번호
    private String birthDate;     // 생년월일 (영수증 주민번호 대체용)
    private LocalDate donationDate; // 기부 날짜 (연월일)

    @Column(name = "item_code")
    private String itemCode;      // 항목 코드 (41: 각종 헌금 등)

    private LocalDateTime createdAt; // 데이터 저장 시점

    public Donation(String name, String memberId, Long amount, String phone, String birthDate, String itemCode) {
        this.name = name;
        this.memberId = memberId;
        this.amount = amount;
        this.phone = phone;
        this.birthDate = birthDate;
        this.itemCode = itemCode;
        this.donationDate = LocalDate.now(); // 오늘 날짜로 저장
        this.createdAt = LocalDateTime.now();
    }
}