package com.church.donation.controller;

import com.church.donation.domain.*;
import com.church.donation.repository.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.church.donation.service.ExcelService;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final MemberRepository memberRepository;
    private final DonationRepository donationRepository; // 헌금(DB) 접근 권한 추가
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final ChurchInfoRepository churchInfoRepository;
    private final ApiSettingsRepository apiSettingsRepository; // api 주입 추가
    private final ExcelService excelService; // 서비스 계층 주입 추가

    // 1. 웹 페이지에서 교인 명부 표로 보기
    @GetMapping("/admin/members")
    public String memberList(Model model) {
        List<Member> members = memberRepository.findAll();
        model.addAttribute("members", members);
        return "admin_members_show"; // 타임리프 HTML 파일 호출
    }

    // 2. 엑셀 다운로드 버튼을 눌렀을 때 실행되는 기능

        // ==========================================
        // 헌금 내역 표로 보기
        // ==========================================
        @GetMapping("/admin/donations")
        public String donationList(Model model) {
            List<Donation> donations = donationRepository.findAll();
            model.addAttribute("donations", donations);
            return "admin_donations_show"; // 타임리프 HTML 파일 호출
        }

        // ==========================================
        //  헌금 내역 엑셀 다운로드
        // ==========================================
        @GetMapping("/admin/excel/members")
    public void downloadMembersExcel(HttpServletResponse response) throws IOException {
        // 복잡한 로직은 모두 Service로 위임! (단일 책임 원칙)
        excelService.exportMembersToExcel(response);
    }

    @GetMapping("/admin/excel/donations")
    public void downloadDonationsExcel(HttpServletResponse response) throws IOException {
        excelService.exportDonationsToExcel(response);
    }
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // templates/login.html 화면을 띄워주는 역할
    }

    // ==========================================
    // [교회 정보 관리] 화면 보여주기
    // ==========================================
    @GetMapping("/admin/settings")
    public String settingsPage(Model model) {
        // DB에서 1번 교회 정보를 찾아보고, 없으면 빈 객체를 넘겨줍니다.
        ChurchInfo churchInfo = churchInfoRepository.findById(1L)
                .orElse(new ChurchInfo("설정안됨", "설정안됨", "설정안됨", "설정안됨", "설정안됨"));

        model.addAttribute("churchInfo", churchInfo);
        return "admin_setting"; // templates/admin_settings.html
    }
    // ==========================================
    // [교회 정보 관리] 수정(덮어쓰기) 처리하기
    // ==========================================
    @PostMapping("/admin/settings")
    public String updateSettings(String name, String repName, String businessNo, String address, String serialNumber) {

        // 1번 데이터를 찾습니다.
        ChurchInfo churchInfo = churchInfoRepository.findById(1L).orElse(new ChurchInfo());

        // 새로운 정보로 덮어씁니다.
        churchInfo.updateInfo(name, repName, businessNo, address, serialNumber);

        // 저장! (JPA는 id가 1번으로 똑같으면 알아서 기존 것을 지우고(Update) 덮어씁니다)
        churchInfoRepository.save(churchInfo);

        return "redirect:/admin/settings?success"; // 수정 완료 후 다시 설정 화면으로!
    }
    // ==========================================
    // [API 연동 설정] 화면 보여주기
    // ==========================================
    @GetMapping("/admin/api-settings")
    public String apiSettingsPage(Model model) {
        ApiSettings apiSettings = apiSettingsRepository.findById(1L)
                .orElse(new ApiSettings("", "", "")); // 없으면 빈 칸으로 시작

        model.addAttribute("apiSettings", apiSettings);
        return "admin_api_settings";
    }

    // ==========================================
    // [API 연동 설정] 수정(덮어쓰기) 처리하기
    // ==========================================
    @PostMapping("/admin/api-settings")
    public String updateApiSettings(String smsApiKey, String smsApiSecret, String senderNumber) {

        ApiSettings apiSettings = apiSettingsRepository.findById(1L).orElse(new ApiSettings());

        apiSettings.updateSettings(smsApiKey, smsApiSecret, senderNumber);

        apiSettingsRepository.save(apiSettings);

        return "redirect:/admin/api-settings?success"; // 성공 시 팝업 띄우기 위해 ?success 붙임
    }

    // ==========================================
    // [관리자 계정 설정] 화면 보여주기
    // ==========================================
    @GetMapping("/admin/account")
    public String accountSettingsPage(Principal principal, Model model) {
        // Principal: 현재 로그인한 사용자의 정보를 담고 있는 스프링 시큐리티 객체입니다.
        Admin admin = adminRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("관리자 정보를 찾을 수 없습니다."));

        model.addAttribute("admin", admin);
        return "admin_account"; // templates/admin_account.html
    }

    // ==========================================
    // [관리자 계정 설정] 변경 처리하기
    // ==========================================
    @PostMapping("/admin/account")
    public String updateAccount(Principal principal, String currentPassword, String newUsername, String newPassword, Model model) {

        Admin admin = adminRepository.findByUsername(principal.getName()).orElseThrow();

        // 🚨 보안 1: 현재 비밀번호가 맞는지 확인 (passwordEncoder.matches 사용!)
        if (!passwordEncoder.matches(currentPassword, admin.getPassword())) {
            model.addAttribute("admin", admin);
            model.addAttribute("error", "현재 비밀번호가 일치하지 않습니다.");
            return "admin_account"; // 틀리면 다시 화면으로 돌려보냄
        }

        // 🚨 보안 2: 비밀번호가 맞으면, 새로운 정보를 암호화해서 덮어쓰기
        admin.updateAccount(newUsername, passwordEncoder.encode(newPassword));
        adminRepository.save(admin);

        // 🚨 보안 3: 정보가 변경되었으므로 시큐리티의 강제 로그아웃 주소로 쫓아냅니다!
        return "redirect:/logout";
    }
}

