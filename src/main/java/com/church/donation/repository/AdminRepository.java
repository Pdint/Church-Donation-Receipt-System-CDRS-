package com.church.donation.repository;

import com.church.donation.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    // 스프링 시큐리티가 로그인 시도 시 "이 아이디 가진 사람 있어?" 하고 DB를 뒤질 때 사용할 가장 핵심적인 명령어.
    Optional<Admin> findByUsername(String username);
}