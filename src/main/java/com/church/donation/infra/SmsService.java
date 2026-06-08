package com.church.donation.infra; // 학우님의 기존 패키지명 그대로 유지!

import com.church.donation.domain.ApiSettings;
import com.church.donation.repository.ApiSettingsRepository;
import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // 레포지토리를 자동으로 주입받기 위해 추가!
public class SmsService {

    // 1. properties 대신, 우리가 만든 DB 수첩을 가져옵니다.
    private final ApiSettingsRepository apiSettingsRepository;

    public void sendVerificationCode(String phone, String code) {

        // 2. 문자를 보내기 직전, DB에서 가장 최신 API 설정을 꺼내옵니다.
        ApiSettings api = apiSettingsRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("관리자 페이지에서 API 연동 설정을 먼저 완료해주세요."));

        // 3. 꺼내온 최신 키를 사용해 그 자리에서 Solapi 클라이언트를 생성합니다.
        // (이렇게 해야 관리자가 화면에서 키를 바꾸면 즉각적으로 새로운 키가 반영됩니다!)
        DefaultMessageService messageService = SolapiClient.INSTANCE.createInstance(api.getSmsApiKey(), api.getSmsApiSecret());

        // Message 객체 생성
        Message message = new Message();
        message.setFrom(api.getSenderNumber()); // DB에 저장된 발신자 번호 적용!
        message.setTo(phone);
        message.setText("인증번호 [" + code + "]를 입력해주세요.");

        try {
            messageService.send(message);
            System.out.println("✅ 문자 발송 성공: " + phone);

        } catch (Exception exception) {
            System.out.println("❌ 시스템 오류: " + exception.getMessage());
            throw new RuntimeException("문자 발송 실패 (Solapi 오류): " + exception.getMessage());
        }
    }

}