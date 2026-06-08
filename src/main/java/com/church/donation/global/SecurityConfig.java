package com.church.donation.global;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 초기 개발을 위해 CSRF 방어 잠시 해제
                .authorizeHttpRequests(auth -> auth
                        // 🔓 누구나 들어올 수 있는 허용 구역 (로그인, 회원가입, 탭 아이콘 등)
                        .requestMatchers(
                                "/CDRS.html",         // 메인 화면
                                "/receipt/**",        // 영수증 화면 (예: /receipt/123, /receipt/search 등)
                                "/login",             // 관리자 로그인 화면
                                "/favicon.ico", "/error",
                                "/",                  //첫 링크
                                "/css/**", "/js/**", "/images/**",
                                "/*.css", "/*.js", "/*.png", "/*.jpg",// 디자인 파일들이 깨지지 않도록 통행 허용!
                                "/api/auth/**", //인증절차 진행 하기 위한 길 터주기
                                "/prayer-board" ,"/api/prayers/**", //중보기도 보안 열어주기
                                "/index.html" // 대문 페이지 얼어주기
                        ).permitAll()
                        // 🔒 그 외의 모든 화면(대시보드, 교인 목록 등)은 무조건 로그인 필수!
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // 우리가 만든 예쁜 로그인 창으로 연결
                        .defaultSuccessUrl("/admin_main.html", true) // 로그인 성공 시 대시보드로 이동
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login") // 로그아웃 성공 시 다시 로그인 창으로
                        .permitAll()
                );

        return http.build();
    }

    // 🔐 비밀번호를 안전하게 암호화해주는 도구 (DB에 1234로 안 들어가고 복잡한 문자로 변환됨)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // (이전에 있던 고정 아이디 admin/1234 코드는 완벽하게 삭제되었습니다!)
}