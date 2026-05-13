package com.church.donation.controller;

// 1. 우리가 만든 클래스들 (패키지명은 본인의 설정에 맞게 확인하세요)
import com.church.donation.domain.Member;
import com.church.donation.repository.MemberRepository;

// 2. 스프링 부트 웹 관련
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostMapping
    public String saveMember(@RequestBody Member member) {
        memberRepository.save(member);
        return "OK";
    }
}