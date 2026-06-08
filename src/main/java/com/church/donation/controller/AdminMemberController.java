package com.church.donation.controller;

import com.church.donation.dto.MemberUpdateRequest;
import com.church.donation.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // JSON 응답을 위해 RestController 사용
@RequestMapping("/api/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final MemberService memberService;

    // 💡 PUT 메서드를 사용하여 데이터 수정 요청을 받습니다.
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