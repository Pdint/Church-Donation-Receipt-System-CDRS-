package com.church.donation.repository;

import com.church.donation.domain.PrayerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrayerRequestRepository extends JpaRepository<PrayerRequest, Long> {

    // 특정 월("2026-06")의 기도 제목 리스트를 최신순(내림차순)으로 가져오기
    List<PrayerRequest> findByTargetMonthOrderByIdDesc(String targetMonth);
}