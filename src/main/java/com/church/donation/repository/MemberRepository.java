package com.church.donation.repository;

import com.church.donation.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    // JpaRepository<엔티티클래스, ID타입>

    // 성함(name)과 생년월일(birthDate)을 동시에 만족하는 교인 검색 (동명이인이 있을 수 있기 떄문에 List 반환)
    List<Member> findByNameAndBirthDate(String name, String birthDate);
}