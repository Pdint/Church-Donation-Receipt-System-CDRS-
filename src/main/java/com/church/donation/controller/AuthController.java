package com.church.donation.controller;

import com.church.donation.dto.SmsRequest;
import com.church.donation.infra.SmsService;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController // 이 클래스가 브라우저의 요청을 받는 곳임을 선언
@RequestMapping("/api/auth") // 공통 주소: http://localhost:8080/api/auth
public class AuthController {

    private final SmsService smsService;

    // 스프링이 자동으로 SmsService를 연결해줍니다 (생성자 주입)
    public AuthController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/send-sms") // 세부 주소: /send-sms
    public String sendSms(@RequestBody SmsRequest request) {
        // 1. 6자리 랜덤 인증번호 생성
        String code = String.valueOf(new Random().nextInt(900000) + 100000);

        // 2. 인프라 패키지의 SmsService를 통해 문자 발송
        smsService.sendVerificationCode(request.getPhone(), code);

        // 3. 브라우저(JS)에 성공 메시지 반환
        return "OK";
    }
}