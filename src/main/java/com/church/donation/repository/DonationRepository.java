package com.church.donation.repository;

import com.church.donation.domain.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    // 특정 교인의 특정 연도 기부 총합을 계산하는 핵심 DB 집계 연산 추가
    @Query("SELECT SUM(d.amount) FROM Donation d WHERE d.memberId = :memberId AND YEAR(d.donationDate) = :year")
    Long sumAmountByMemberIdAndYear(@Param("memberId") String memberId, @Param("year") int year);

}