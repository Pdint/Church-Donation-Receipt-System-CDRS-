package com.church.donation.controller;

import com.church.donation.repository.DonationRepository;
import com.church.donation.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller //  1. "나는 화면 이동을 제어하는 컨트롤러야!"
@RequiredArgsConstructor //  2. 창고 관리인(Repository)들을 자동으로 주입해줌
public class DeleteController {

    // 🌟 3. 삭제 작업에 필요한 레포지토리 2개 불러오기
    private final DonationRepository donationRepository;
    private final MemberRepository memberRepository;

    // ==========================================
    // 🗑️ 1. [기부금 내역 단건 삭제]
    // ==========================================
    @PostMapping("/admin/donations/delete/{id}")
    public String deleteDonation(@PathVariable Long id) {
        donationRepository.deleteById(id);

        // 삭제 완료 후 기부금 목록 화면으로 돌아가기 (경로 확인 필요)
        return "redirect:/admin/donations";
    }

    // ==========================================
    // 🗑️ 2. [교인 전체 삭제 (기부금 포함)]
    // ==========================================
    @PostMapping("/admin/members/delete/{memberId}")
    @Transactional // 4. 기부금 삭제와 교인 삭제가 세트로 완벽하게 묶여서 처리되도록 보장!
    public String deleteMember(@PathVariable String memberId) {

        // (1) 먼저 이 교인이 낸 헌금 내역부터 모두 청소
        donationRepository.deleteByMemberId(memberId);

        // (2) 헌금 내역이 지워졌으니 안전하게 교인 정보 삭제
        memberRepository.deleteById(memberId);

        // 삭제 완료 후 교인 명부 화면으로 돌아가기 (경로 확인 필요)
        return "redirect:/admin/members";
    }
}