package com.church.donation.controller;

// 1. 우리가 만든 클래스들 (패키지명은 본인의 설정에 맞게 확인하세요)
import com.church.donation.domain.Member;
import com.church.donation.repository.MemberRepository;

// 2. 스프링 부트 웹 관련
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostMapping
    public ResponseEntity<String> saveMember(@RequestBody Member member) {

        // 🚨 [예외 처리] 서버단 빈 값 체크
        if (member.getMemberId() == null || member.getMemberId().trim().isEmpty() ||
                member.getName() == null || member.getName().trim().isEmpty() ||
                member.getBirthDate() == null || member.getBirthDate().trim().isEmpty() ||
                member.getPhone() == null || member.getPhone().trim().isEmpty()) {

            // 하나라도 비어있다면 400 에러와 함께 메시지 반환
            return ResponseEntity.badRequest().body("모든 항목을 기입해주세요.");
        }

        // 정상적으로 모두 기입되었다면 저장
        memberRepository.save(member);

        // 200 OK 반환
        return ResponseEntity.ok("OK");
    }
}