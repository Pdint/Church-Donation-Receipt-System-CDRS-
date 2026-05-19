package com.church.donation.controller;

import com.church.donation.domain.Member;
import com.church.donation.repository.MemberRepository;
import com.church.donation.repository.DonationRepository;
import com.church.donation.dto.ReceiptResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ReceiptController {

    private final MemberRepository memberRepository;
    private final DonationRepository donationRepository;

    @GetMapping("/receipt/{memberId}")
    public String generateReceipt(@PathVariable String memberId,
                                  @RequestParam(defaultValue = "2026") int year,
                                  Model model) {

        // 1. 교인 마스터 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 교인이 아닙니다."));

        // 2. 해당 연도 헌금 총합 조회
        Long totalAmount = donationRepository.sumAmountByMemberIdAndYear(memberId, year);
        if (totalAmount == null) totalAmount = 0L; // 헌금 내역이 없을 경우 0원 처리

        // 3. 영수증 DTO 조립 (코드는 41 고정)
        ReceiptResponse receipt = ReceiptResponse.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .birthDate(member.getBirthDate())
                .itemCode("41")
                .itemName("각종 헌금")
                .dateRange(year + ".01.01 ~ " + year + ".12.31")
                .totalAmount(totalAmount)
                .build();

        // 4. HTML로 데이터 전달
        model.addAttribute("data", receipt);

        return "receipt.html"; // src/main/resources/templates/receipt.html.html 호출
    }
}