package com.church.donation.controller;

import com.church.donation.dto.SmsRequest;
import com.church.donation.dto.AuthVerifyRequest;
import com.church.donation.domain.Member;
import com.church.donation.repository.MemberRepository;
import com.church.donation.infra.SmsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final SmsService smsService;
    private final MemberRepository memberRepository;

    public AuthController(SmsService smsService, MemberRepository memberRepository) {
        this.smsService = smsService;
        this.memberRepository = memberRepository;
    }

    @PostMapping("/send-sms")
    public String sendSms(@RequestBody SmsRequest request) {
        String code = String.valueOf(new Random().nextInt(900000) + 100000);
        smsService.sendVerificationCode(request.getPhone(), code);
        return "OK";
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody AuthVerifyRequest request) {

        // Optional이 아닌 List<Member> 구조로 데이터를 받아옵니다.
        List<Member> members = memberRepository.findByNameAndBirthDate(request.getName(), request.getBirthDate());

        //[핵심] List 객체의 수량에 맞춰 안전하게 스마트 분기 처리를 진행합니다. (orElseThrow 제거 완료)
        if (members.isEmpty()) {
            return ResponseEntity.badRequest().body("일치하는 교인 정보가 마스터 DB에 없습니다.");
        } else if (members.size() > 1) {
            // 성함과 생년월일이 똑같은 동명이인이 실존하는 치명적인 예외 상황 제어
            return ResponseEntity.badRequest().body("시스템에 동일한 성함과 생년월일을 가진 교인이 존재합니다. 관리자에게 문의하여 교인 ID를 확인해 주세요.");
        }

        // 리스트에 정확히 1명만 존재하므로 안전하게 첫번째 index에서 교인 객체를 추출합니다.
        Member exactMember = members.getFirst();

        // 추출된 교인 정보(memberId 포함)를 프론트엔드(script.js)로 리턴하여 즉시 리다이렉션을 유도합니다.
        return ResponseEntity.ok(exactMember);
    }
}