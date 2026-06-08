package com.church.donation.service;

import com.church.donation.domain.PrayerRequest;
import com.church.donation.repository.PrayerRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용으로 세팅 (조회 속도 향상)
public class PrayerRequestService {

    private final PrayerRequestRepository prayerRepository;

    // 1. [저장] 새 기도제목 올리기
    @Transactional // 쓰는 작업에는 다시 트랜잭션 걸어주기
    public PrayerRequest createPrayer(PrayerRequest request) {
        return prayerRepository.save(request);
    }

    // 2. [조회] 특정 월의 리스트 가져오기
    public List<PrayerRequest> getPrayersByMonth(String month) {
        return prayerRepository.findByTargetMonthOrderByIdDesc(month);
    }

    // 3. [삭제] 비밀번호 및 권한 검증 후 삭제
    @Transactional
    public void deletePrayer(Long id, String inputPassword, boolean isAdmin) {
        // 일단 DB에서 해당 글을 찾습니다.
        PrayerRequest prayer = prayerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("이미 삭제되었거나 존재하지 않는 기도 제목입니다."));

        // 관리자가 아니라면? ➔ 비밀번호가 맞는지 검사!
        if (!isAdmin) {
            if (!prayer.getPassword().equals(inputPassword)) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        }

        // 검증을 무사히 통과했다면 삭제!
        prayerRepository.delete(prayer);
    }
}