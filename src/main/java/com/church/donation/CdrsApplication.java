package com.church.donation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing  // 이 스위치를 반드시 달아주어야 시간이 자동으로 들어갑니다!!
@SpringBootApplication
public class CdrsApplication {
    public static void main(String[] args) {
        SpringApplication.run(CdrsApplication.class, args);
    }
}