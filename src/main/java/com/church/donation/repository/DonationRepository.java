package com.church.donation.repository;

import com.church.donation.domain.Donation;
import com.church.donation.dto.MonthlyStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    // 특정 교인의 특정 연도 기부 총합을 계산하는 핵심 DB 집계 연산 추가
    @Query("SELECT SUM(d.amount) FROM Donation d WHERE d.memberId = :memberId AND YEAR(d.donationDate) = :year")
    Long sumAmountByMemberIdAndYear(@Param("memberId") String memberId, @Param("year") int year);

    // 특정 연도의 월별 헌금 총합을 가져오는 그룹(GROUP BY) 쿼리
    @Query("SELECT MONTH(d.donationDate) AS month, SUM(d.amount) AS total " +
            "FROM Donation d WHERE YEAR(d.donationDate) = :year " +
            "GROUP BY MONTH(d.donationDate) ORDER BY month")
    List<MonthlyStat> findMonthlyTotalByYear(@Param("year") int year);

    // 특정 교인의 모든 기부금 내역을 한 번에 삭제하는 마법의 메서드
    @Transactional
    void deleteByMemberId(String memberId);
}