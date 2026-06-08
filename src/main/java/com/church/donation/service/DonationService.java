package com.church.donation.service;

import com.church.donation.domain.Donation;
import com.church.donation.domain.Member;
import com.church.donation.dto.DonationRequest;
import com.church.donation.repository.DonationRepository;
import com.church.donation.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository donationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveBulkDonations(List<DonationRequest> requests) {
        List<Donation> donations = new ArrayList<>();

        for (DonationRequest req : requests) {
            // 빈 데이터 통과
            if (req.getMemberId() == null || req.getMemberId().trim().isEmpty() || req.getAmount() == null) {
                continue;
            }

            // 1. 넘어온 memberId로 즉시 교인 단건 조회 (속도 최고)
            Member member = memberRepository.findById(req.getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 교인입니다."));

            // 2. 헌금 엔티티 조립
            Donation donation = new Donation(
                    member.getMemberId(),
                    req.getAmount(),
                    req.getDonationDate()
            );

            donations.add(donation);
        }

        // 3. 일괄 저장 (Bulk Insert)
        if (!donations.isEmpty()) {
            donationRepository.saveAll(donations);
        }
    }
}