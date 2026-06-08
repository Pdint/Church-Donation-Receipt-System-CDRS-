package com.church.donation.service;

import com.church.donation.domain.Member;
import com.church.donation.dto.MemberUpdateRequest;
import com.church.donation.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 💡 관리자가 교인 정보를 수정할 때 사용하는 핵심 로직
    @Transactional // JPA의 더티 체킹을 사용하기 위해 반드시 필요합니다!
    public void updateMemberInfo(String memberId, MemberUpdateRequest request) {

        // 1. 기존 교인 정보를 DB에서 꺼내옵니다.
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 교인입니다."));

        // 2. 전달받은 새로운 정보로 기존 객체의 데이터를 덮어씌웁니다.
        // (이때 양옆 공백이나 전화번호의 하이픈을 제거하는 정제 작업도 함께 해줍니다)
        member.setName(request.getName().trim());
        member.setBirthDate(request.getBirthDate().trim());
        member.setPhone(request.getPhone().replace("-", "").trim());

        // 3. @Transactional 마법: 따로 memberRepository.save(member)를 하지 않아도,
        // 스프링이 데이터 변경을 감지하고 알아서 DB에 UPDATE 쿼리를 날려줍니다.
    }
}