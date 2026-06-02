package com.church.donation.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // DB 내부 관리용 고유 번호

    // 로그인에 사용할 아이디 (중복 가입 불가)
    @Column(unique = true, nullable = false)
    private String username;

    // 암호화된 비밀번호가 저장될 곳
    @Column(nullable = false)
    private String password;

    // 관리자 이름 (예: 김간사, 이목사)
    private String name;

    // 소속 교회 이름 (여러 교회가 쓸 경우를 대비한 확장성!)
    private String churchName;

    // 권한 (기본값으로 관리자를 뜻하는 "ROLE_ADMIN"을 부여합니다)
    private String role;

    @Builder
    public Admin(String username, String password, String name, String churchName, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.churchName = churchName;
        this.role = role != null ? role : "ROLE_ADMIN";
    }
    // 🌟 관리자 아이디/비밀번호 변경을 위한 메서드 추가
    public void updateAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }
}