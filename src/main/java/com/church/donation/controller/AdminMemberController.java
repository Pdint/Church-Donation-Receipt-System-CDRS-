package com.church.donation.controller;

import com.church.donation.domain.Member; // 💡 추가
import com.church.donation.dto.MemberUpdateRequest;
import com.church.donation.repository.MemberRepository; // 💡 추가
import com.church.donation.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List; // 💡 추가

@RestController
@RequestMapping("/api/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository; // 💡 [추가] 교인 목록 조회를 위해 필요합니다!

    // =========================================================
    // 💡 1. [새로 추가] 프론트엔드의 fetch('/api/admin/members/all')을 받아줍니다.
    // =========================================================
    @GetMapping("/all")
    public ResponseEntity<List<Member>> getAllMembers() {
        // 모든 교인 정보를 찾아서 프론트엔드 드롭다운으로 쏴줍니다.
        return ResponseEntity.ok(memberRepository.findAll());
    }

    // =========================================================
    // 2. [기존 코드 유지] 교인 정보 수정 기능
    // =========================================================
    @PutMapping("/{memberId}")
    public ResponseEntity<String> updateMember(@PathVariable String memberId,
                                               @RequestBody MemberUpdateRequest request) {
        try {
            memberService.updateMemberInfo(memberId, request);
            return ResponseEntity.ok("success");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}