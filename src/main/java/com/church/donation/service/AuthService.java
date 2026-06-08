package com.church.donation.service;

import com.church.donation.domain.Member;
import com.church.donation.dto.AuthVerifyRequest;
import com.church.donation.infra.SmsService;
import com.church.donation.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final SmsService smsService;

    // 인증 및 문자 발송 비즈니스 로직
    public String verifyAndSendSms(AuthVerifyRequest request) {

        // 양옆 공백 제거 및 전화번호 하이픈 제거
        String cleanName = request.getName() != null ? request.getName().trim() : "";
        String cleanBirth = request.getBirthDate() != null ? request.getBirthDate().trim() : "";
        String cleanPhone = request.getPhone() != null ? request.getPhone().replace("-", "").trim() : "";

        // 2. 정제된 데이터로 DB 조회
        Optional<Member> memberOpt = memberRepository.findByNameAndBirthDateAndPhone(
                cleanName, cleanBirth, cleanPhone
        );

        if (memberOpt.isEmpty()) {
            throw new IllegalArgumentException("일치하는 교인 정보가 없습니다.");
        }

        // 2. 인증번호 6자리 난수 생성
        String verificationCode = String.format("%06d", new Random().nextInt(1000000));

        // 3. SMS 발송 지시
        String message = "본인확인 인증번호는 [" + verificationCode + "] 입니다.";
        smsService.sendVerificationCode(request.getPhone(), message);

        // 4. 인증번호 반환 (프론트엔드 검증용)
        return verificationCode;


    }
    public String getMemberId(AuthVerifyRequest request) {
        String cleanName = request.getName() != null ? request.getName().trim() : "";
        String cleanBirth = request.getBirthDate() != null ? request.getBirthDate().trim() : "";
        String cleanPhone = request.getPhone() != null ? request.getPhone().replace("-", "").trim() : "";

        Member member = memberRepository.findByNameAndBirthDateAndPhone(cleanName, cleanBirth, cleanPhone)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 교인 정보가 없습니다."));

        return member.getMemberId();
    }
}