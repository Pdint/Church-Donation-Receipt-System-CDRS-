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

    // 1. 웹 페이지에서 교인 명부 표로 보기
    @GetMapping("/admin/members")
    public String memberList(Model model) {
        List<Member> members = memberRepository.findAll();
        model.addAttribute("members", members);
        return "admin_members_show"; // 타임리프 HTML 파일 호출
    }

    // 2. 엑셀 다운로드 버튼을 눌렀을 때 실행되는 기능
    @GetMapping("/admin/members/excel")
    public void downloadExcel(HttpServletResponse response) throws IOException {
        List<Member> members = memberRepository.findAll();

        // 빈 엑셀 파일 생성
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("교인 명부");
        Row headerRow = sheet.createRow(0);

        // 첫 번째 줄(헤더) 제목 달기
        headerRow.createCell(0).setCellValue("교인 ID");
        headerRow.createCell(1).setCellValue("이름");
        headerRow.createCell(2).setCellValue("생년월일");
        headerRow.createCell(3).setCellValue("전화번호");
        headerRow.createCell(4).setCellValue("가입일자");

        // DB 데이터 엑셀에 채워 넣기
        int rowNum = 1;
        for (Member member : members) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(member.getMemberId());
            row.createCell(1).setCellValue(member.getName());
            row.createCell(2).setCellValue(member.getBirthDate());
            row.createCell(3).setCellValue(member.getPhone());
            // 시간이 널(null)이 아니면 문자열로 변환해서 넣기
            row.createCell(4).setCellValue(member.getCreatedAt() != null ? member.getCreatedAt().toString() : "");
        }

        // 브라우저가 "아, 이건 엑셀 파일 다운로드구나!" 하고 인식하게 만드는 설정
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=members.xlsx");

        // 엑셀 파일 내보내기
        workbook.write(response.getOutputStream());
        workbook.close();
    }
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
        @GetMapping("/admin/donations/excel")
        public void downloadDonationExcel(HttpServletResponse response) throws IOException {
            List<Donation> donations = donationRepository.findAll();

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("헌금 내역");
            Row headerRow = sheet.createRow(0);

            // 첫 번째 줄(헤더) 제목 달기
            headerRow.createCell(0).setCellValue("교인 ID");
            headerRow.createCell(1).setCellValue("기부 금액(원)");
            headerRow.createCell(2).setCellValue("기부 날짜");
            headerRow.createCell(3).setCellValue("시스템 기록 시간");

            // DB 데이터 엑셀에 채워 넣기
            int rowNum = 1;
            for (Donation donation : donations) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(donation.getMemberId());
                row.createCell(1).setCellValue(donation.getAmount());
                row.createCell(2).setCellValue(donation.getDonationDate() != null ? donation.getDonationDate().toString() : "");
                row.createCell(3).setCellValue(donation.getCreatedAt() != null ? donation.getCreatedAt().toString() : "");
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=donations.xlsx");

            workbook.write(response.getOutputStream());
            workbook.close();
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

