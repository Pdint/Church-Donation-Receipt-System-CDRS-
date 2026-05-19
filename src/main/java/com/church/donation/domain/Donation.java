package com.church.donation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Donation extends BaseTimeEntity { // 👈 상속을 통해 중복 등록 시간 코드 완벽 제거!

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 동일 인물이 같은 날 여러 번 기부할 수 있으므로 '기부 건수'를 식별할 자동 증가 PK 유지

    private String memberId; // Member 엔티티의 memberId와 논리적으로 연결되는 외래키(FK) 역할 역할
    private Long amount;
    private LocalDate donationDate; // 실제 기부금 영수증에 찍힐 헌금 정산 날짜

    // 👈 [수정] 클래스 내부 중복 createdAt 필드 및 생성자 수동 대입 로직 완벽 제거 완료

    public Donation(String memberId, Long amount, String donationDate) {
        this.memberId = memberId;
        this.amount = amount;
        // 프론트엔드 화면에서 넘어온 날짜 문자열("yyyy-MM-dd")을 영속성 처리가 가능한 LocalDate 객체로 파싱
        this.donationDate = LocalDate.parse(donationDate);
    }
}