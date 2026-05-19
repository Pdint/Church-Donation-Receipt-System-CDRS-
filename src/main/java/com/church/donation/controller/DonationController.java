package com.church.donation.controller;

import com.church.donation.domain.Donation;
import com.church.donation.domain.Member;
import com.church.donation.dto.DonationRequest;
import com.church.donation.repository.DonationRepository;
import com.church.donation.repository.MemberRepository; // 👈 1. 임포트 추가
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/donation")
public class DonationController {

    private final DonationRepository donationRepository;
    private final MemberRepository memberRepository; // 👈 2. 심볼 해결을 위해 필드 변수 선언 추가!

    // 👈 3. 스프링 부트가 MemberRepository를 알아서 주입(의존성 주입)하도록 생성자 수정!
    public DonationController(DonationRepository donationRepository, MemberRepository memberRepository) {
        this.donationRepository = donationRepository;
        this.memberRepository = memberRepository;
    }

    @PostMapping("/batch")
    public String saveBatch(@RequestBody List<DonationRequest> requests) {

        // Request DTO 리스트를 Entity 리스트로 변환하면서 스마트 검증 프로세스 수행
        List<Donation> donations = requests.stream()
                .map(req -> {
                    // 이름과 생년월일로 검색하여 List 형태로 교인 목록을 먼저 받아옵니다.
                    List<Member> members = memberRepository.findByNameAndBirthDate(req.getName(), req.getBirthDate());

                    // 👈 4. List 객체에 맞게 로직을 분기하여 orElseThrow 에러 완벽 해결!
                    if (members.isEmpty()) {
                        throw new IllegalArgumentException(req.getName() + "님의 교인 정보가 마스터 DB에 없습니다.");
                    } else if (members.size() > 1) {
                        // 같은 이름, 같은 생일인 교인이 2명 이상 시스템에 존재할 때 동명이인 예외 방어
                        throw new IllegalArgumentException(req.getName() + "님은 동명이인(생년월일 동일)이 존재하여 일괄 등록이 불가능합니다. 관리자 확인이 필요합니다.");
                    }

                    // 리스트에 정확히 1명만 존재하므로 안전하게 0번째 index에서 교인 마스터 객체를 꺼냅니다.
                    Member exactMember = members.get(0);

                    // 알아서 매핑된 고유 memberId를 물고 Donation 엔티티를 빌드합니다.
                    return new Donation(
                            exactMember.getMemberId(),
                            req.getAmount(),
                            req.getDonationDate()
                    );
                })
                .collect(Collectors.toList());

        // 한 번에 DB 대량 저장 (Bulk Save)
        donationRepository.saveAll(donations);
        return "SUCCESS";
    }
}