package com.church.donation.repository;

import com.church.donation.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    // JpaRepository<엔티티클래스, ID타입>

    // 👈 성함(name)과 생년월일(birthDate)을 동시에 만족하는 교인 검색
    Optional<Member> findByNameAndBirthDate(String name, String birthDate);
}