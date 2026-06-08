package com.church.donation.controller;

import com.church.donation.dto.AuthVerifyRequest;
import com.church.donation.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/send-sms")
    public ResponseEntity<String> sendSms(@RequestBody AuthVerifyRequest request) {
        try {
            // 모든 비즈니스 로직은 Service가 처리
            String code = authService.verifyAndSendSms(request);
            return ResponseEntity.ok(code);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody AuthVerifyRequest request) {
        try {
            // 1. 서비스에서 교인을 찾아 memberId를 가져옴
            String memberId = authService.getMemberId(request);

            // 2. 프론트엔드가 memberData.memberId 로 읽을 수 있게 Map(JSON)으로 반환
            return ResponseEntity.ok(Map.of("memberId", memberId));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}