package com.church.donation.service;

import com.church.donation.domain.ChurchInfo;
import com.church.donation.domain.Member;
import com.church.donation.dto.ReceiptResponse;
import com.church.donation.repository.ChurchInfoRepository;
import com.church.donation.repository.DonationRepository;
import com.church.donation.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReceiptService {

    private final MemberRepository memberRepository;
    private final DonationRepository donationRepository;
    private final ChurchInfoRepository churchInfoRepository;

    // 💡 [핵심 수정] 컨트롤러로부터 연도(int year)를 함께 넘겨받도록 수정합니다.
    public ReceiptResponse generateReceiptData(String memberId, int year) {

        // 1. 교인 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("교인을 찾을 수 없습니다."));

        // 2. [성능 고도화] DB에서 직접 특정 교인의 특정 연도 헌금 총합을 쿼리로 가져옵니다!
        Long totalAmount = donationRepository.sumAmountByMemberIdAndYear(memberId, year);

        // 만약 해당 연도에 헌금 내역이 없으면 null이 반환되므로 0L로 안전하게 처리합니다.
        if (totalAmount == null) {
            totalAmount = 0L;
        }

        // 3. 교회 설정 정보 조회
        ChurchInfo churchInfo = churchInfoRepository.findById(1L).orElse(new ChurchInfo());

        // 4. DTO에 값 세팅
        ReceiptResponse response = new ReceiptResponse();
        response.setMember(member);
        response.setTotalAmount(totalAmount);
        response.setChurchInfo(churchInfo);

        return response;
    }
}