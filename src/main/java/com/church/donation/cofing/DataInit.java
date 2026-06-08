package com.church.donation.cofing;

import com.church.donation.domain.Admin;
import com.church.donation.domain.DonationCategory;
import com.church.donation.domain.Member;
import com.church.donation.domain.Donation;
import com.church.donation.repository.AdminRepository;
import com.church.donation.repository.DonationCategoryRepository;
import com.church.donation.repository.MemberRepository;
import com.church.donation.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final DonationCategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final DonationRepository donationRepository;

    @Value("${init.admin.username}")
    private String initUsername;

    @Value("${init.admin.password}")
    private String initPassword;

    @Override
    public void run(String... args) throws Exception {

        // 🧹 기존 더미 데이터 청소
        System.out.println("🧹 [시스템 알림] 기존 더미 데이터를 완전히 청소합니다...");
        donationRepository.deleteAll();
        memberRepository.deleteAll();
        categoryRepository.deleteAll();

        // 1. 최고 관리자 계정 생성
        if (adminRepository.count() == 0) {
            Admin superAdmin = Admin.builder()
                    .username(initUsername)
                    .password(passwordEncoder.encode(initPassword))
                    .role("ROLE_SUPER_ADMIN")
                    .build();
            adminRepository.save(superAdmin);
        }

        // 2. 헌금 카테고리 재생성
        categoryRepository.save(new DonationCategory("십일조"));
        categoryRepository.save(new DonationCategory("주일헌금"));
        categoryRepository.save(new DonationCategory("감사헌금"));
        categoryRepository.save(new DonationCategory("건축헌금"));
        categoryRepository.save(new DonationCategory("선교헌금"));
        categoryRepository.save(new DonationCategory("기타헌금"));

        // 3. 🚀 대규모 더미 데이터(100명, 8000건) 무조건 재생성
        System.out.println("⏳ [시스템 알림] 깨끗해진 DB에 교인 100명과 헌금 8,000건을 새로 생성합니다...");

        List<Member> members = new ArrayList<>();
        Random random = new Random();

        String[] lastNames = {"김", "이", "박", "최", "정", "강", "조", "윤", "장", "임", "황", "한", "오", "서", "신"};
        String[] firstNames = {"민준", "서연", "도윤", "서윤", "시우", "지우", "민재", "하윤", "은우", "시아", "재욱", "지아", "우진", "다은", "건우"};

        Member testVip = new Member();
        testVip.setMemberId("M9999");            // 겹치지 않는 특수 ID
        testVip.setName("황재욱");           // 검색하기 쉬운 이름
        testVip.setBirthDate("040216");          // 고정 생년월일
        testVip.setPhone("01095129703");         // 외우기 쉬운 전화번호!
        members.add(testVip);

        // [1] 교인 100명 생성
        for (int i = 0; i < 100; i++) {
            Member m = new Member();

            // 💡 String 타입의 MemberId를 수동으로 지정 (M0001 ~ M0100)
            m.setMemberId("M" + String.format("%04d", i + 1));

            String randomName = lastNames[random.nextInt(lastNames.length)] + firstNames[random.nextInt(firstNames.length)];
            m.setName(randomName);

            int year = 70 + random.nextInt(30);
            int month = 1 + random.nextInt(12);
            int day = 1 + random.nextInt(28);
            m.setBirthDate(String.format("%02d%02d%02d", year, month, day));

            String randomPhone = String.format("010%04d%04d", random.nextInt(10000), random.nextInt(10000));
            m.setPhone(randomPhone);

            members.add(m);
        }
        memberRepository.saveAll(members);

        // [2] 헌금 8,000건 생성 (1명당 80건)
        List<Donation> donations = new ArrayList<>();
        String[] types = {"십일조", "주일헌금", "감사헌금", "건축헌금", "선교헌금", "기타헌금"};

        for (Member m : members) {
            for (int j = 0; j < 80; j++) {
                Donation d = new Donation();
                d.setMember(m); // 👈 이제 m 내부의 memberId가 무조건 존재하므로 에러 안 남!
                d.setDonationType(types[random.nextInt(types.length)]);

                long randomAmount = (random.nextInt(50) + 1) * 10000L;
                d.setAmount(randomAmount);

                int dYear = 2025 + random.nextInt(2);
                int dMonth = 1 + random.nextInt(12);
                int dDay = 1 + random.nextInt(28);
                d.setDonationDate(String.format("%04d-%02d-%02d", dYear, dMonth, dDay));

                donations.add(d);
            }
        }
        donationRepository.saveAll(donations);

        System.out.println("✅ [시스템 알림] 내부 데이터 초기화 및 8,000건 재생성이 완벽하게 완료되었습니다!");
    }
}