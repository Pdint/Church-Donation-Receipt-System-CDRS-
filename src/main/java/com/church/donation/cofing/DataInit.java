package com.church.donation.cofing;

import com.church.donation.domain.Admin;
import com.church.donation.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    // application.properties에 방금 적은 값들을 쏙쏙 빼옵니다!
    @Value("${init.admin.username}")
    private String initUsername;

    @Value("${init.admin.password}")
    private String initPassword;

    // 스프링 부트 서버가 켜질 때 딱 한 번 자동으로 실행되는 메서드입니다.
    @Override
    public void run(String... args) throws Exception {

        // 1. DB를 뒤져봤는데 관리자가 1명도 없다면?
        if (adminRepository.count() == 0) {

            // 2. properties 파일에서 읽어온 정보로 최고 관리자 계정을 생성!
            Admin superAdmin = Admin.builder()
                    .username(initUsername)
                    .password(passwordEncoder.encode(initPassword)) // 비번은 든든하게 암호화!
                    .role("ROLE_SUPER_ADMIN")
                    .build();

            adminRepository.save(superAdmin);
            System.out.println("✅ [시스템 알림] 설정 파일을 읽어 초기 최고 관리자 계정이 자동 생성되었습니다.");
        }
    }
}