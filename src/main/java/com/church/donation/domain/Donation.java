package com.church.donation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter; // 👈 추가!
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Getter
@Setter // 👈 추가!
@NoArgsConstructor
public class Donation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberId;
    private Long amount;
    private LocalDate donationDate;
    private String donationType;

    public Donation(String memberId, Long amount, String donationDate) {
        this.memberId = memberId;
        this.amount = amount;
        this.donationDate = LocalDate.parse(donationDate);
    }

    public Donation(String memberId, String donationType, Long amount, String donationDate) {
        this.memberId = memberId;
        this.donationType = donationType;
        this.amount = amount;
        this.donationDate = LocalDate.parse(donationDate);
    }

    // =========================================================================
    // 💡 [핵심] 기존 DataInit.java와 완벽 호환되게 만들어주는 마법의 편의 메서드 2개
    // =========================================================================

    // 1. DataInit에서 setMember(Member객체)로 찔러넣어도 알아서 ID만 쏙 빼서 저장합니다.
    public void setMember(Member member) {
        this.memberId = member.getMemberId();
    }

    // 2. DataInit에서 날짜를 문자열("2026-05-01")로 던져도 알아서 LocalDate로 파싱해서 저장합니다.
    public void setDonationDate(String donationDate) {
        this.donationDate = LocalDate.parse(donationDate);
    }
}