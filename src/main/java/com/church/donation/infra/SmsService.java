package com.church.donation.infra;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class SmsService {

    private DefaultMessageService messageService;

    @Value("${solapi.api-key}")
    private String apiKey;

    @Value("${solapi.api-secret}")
    private String apiSecret;

    @Value("${solapi.sender-number}")
    private String senderNumber;

    @PostConstruct
    public void init() {
        this.messageService = SolapiClient.INSTANCE.createInstance(apiKey, apiSecret);
    }

    public void sendVerificationCode(String phone, String code) {
        // Message 객체 생성 (패키지 중복 방지를 위해 전체 경로 사용 가능)
        Message message = new Message();
        message.setFrom(senderNumber);
        message.setTo(phone);
        message.setText("인증번호 [" + code + "]를 입력해주세요.");

        try {
            // 실제 발송 메소드 호출
            this.messageService.send(message);
            System.out.println("✅ 문자 발송 성공: " + phone);

        } catch (SolapiMessageNotReceivedException exception) {
            // 발송 실패 시 상세 목록 확인
            System.out.println("❌ 발송 실패 메시지 목록: " + exception.getFailedMessageList());
            System.out.println("에러 메시지: " + exception.getMessage());
        } catch (Exception exception) {
            // 기타 일반 예외 처리
            System.out.println("❌ 시스템 오류: " + exception.getMessage());
        }
    }
}