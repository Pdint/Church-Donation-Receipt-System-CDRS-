package com.church.donation.repository;

import com.church.donation.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    // JpaRepository<엔티티클래스, ID타입>
}