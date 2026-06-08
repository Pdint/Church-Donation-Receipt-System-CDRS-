package com.church.donation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PrayerRequest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authorName; // 작성자 이름

    private String password;   // 글 수정/삭제를 위한 비밀번호 (숫자 4자리 등)

    @Column(columnDefinition = "TEXT")
    private String content;    // 기도 제목 내용

    private String targetMonth; // 표출 기준 월 (예: "2026-06")

    // 편의성을 위한 생성자
    public PrayerRequest(String authorName, String password, String content, String targetMonth) {
        this.authorName = authorName;
        this.password = password;
        this.content = content;
        this.targetMonth = targetMonth;
    }
}