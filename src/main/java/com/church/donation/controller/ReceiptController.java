package com.church.donation.controller;

import com.church.donation.domain.ChurchInfo;
import com.church.donation.domain.Member;
import com.church.donation.repository.MemberRepository;
import com.church.donation.repository.DonationRepository;
import com.church.donation.dto.ReceiptResponse;
import com.church.donation.repository.ChurchInfoRepository;

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
    private final ChurchInfoRepository churchInfoRepository; // 완벽하게 주입됨!

    // 이 주소 하나로 기부자 정보와 교회 정보를 모두 챙겨서 화면으로 보냅니다!
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

        // 3. 기부자 영수증 DTO 조립
        ReceiptResponse receipt = ReceiptResponse.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .birthDate(member.getBirthDate())
                .itemCode("41")
                .itemName("각종 헌금")
                .dateRange(year + ".01.01 ~ " + year + ".12.31")
                .totalAmount(totalAmount)
                .build();

        // 4. DB에서 교회 정보 꺼내기 (없으면 임시 깡통 데이터)
        ChurchInfo churchInfo = churchInfoRepository.findById(1L)
                .orElse(new ChurchInfo("미설정", "미설정", "미설정", "미설정", "미설정"));

        // 5. 조립된 두 가지 데이터를 모두 HTML로 쏴줍니다!
        model.addAttribute("data", receipt);      // 기부자 정보
        model.addAttribute("church", churchInfo); // 교회 정보

        return "receipt"; // receipt.html 화면 띄우기
    }
}