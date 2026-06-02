package com.church.donation.service;

import com.church.donation.domain.Admin;
import com.church.donation.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. 로그인 창에 입력된 아이디(username)로 DB를 뒤집니다.
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 아이디를 찾을 수 없습니다: " + username));

        // 2. DB에서 찾은 정보를 시큐리티에게 넘겨줍니다. (비밀번호가 맞는지는 시큐리티가 알아서 비교함!)
        return User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .roles(admin.getRole().replace("ROLE_", "")) // "ROLE_ADMIN" -> "ADMIN"
                .build();
    }
}