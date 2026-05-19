package com.church.donation.controller;

import com.church.donation.dto.SmsRequest;
import com.church.donation.dto.AuthVerifyRequest;
import com.church.donation.domain.Member;
import com.church.donation.repository.MemberRepository;
import com.church.donation.infra.SmsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        // 👈 프론트에서 보낸 [성함]과 [생년월일]로 교인 정보 조회
        Member member = memberRepository.findByNameAndBirthDate(request.getName(), request.getBirthDate())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 교인 정보가 없습니다."));

        return ResponseEntity.ok(member);
    }
}