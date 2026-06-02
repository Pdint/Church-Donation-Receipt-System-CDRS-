package com.church.donation.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ApiSettings {

    @Id
    private Long id = 1L; // 항상 1번 데이터만 사용!

    private String smsApiKey;    // Solapi 등 SMS API 키
    private String smsApiSecret; // SMS API 시크릿 키
    private String senderNumber; // 문자 발송 시 찍힐 발신자 번호

    @Builder
    public ApiSettings(String smsApiKey, String smsApiSecret, String senderNumber) {
        this.id = 1L;
        this.smsApiKey = smsApiKey;
        this.smsApiSecret = smsApiSecret;
        this.senderNumber = senderNumber;
    }

    // 덮어쓰기용 메서드
    public void updateSettings(String smsApiKey, String smsApiSecret, String senderNumber) {
        this.smsApiKey = smsApiKey;
        this.smsApiSecret = smsApiSecret;
        this.senderNumber = senderNumber;
    }
}