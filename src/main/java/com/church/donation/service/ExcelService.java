package com.church.donation.service;

import com.church.donation.domain.Donation;
import com.church.donation.domain.Member;
import com.church.donation.repository.DonationRepository;
import com.church.donation.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 읽기 전용 트랜잭션으로 성능 최적화
public class ExcelService {

    private final MemberRepository memberRepository;
    private final DonationRepository donationRepository;

    // 1. 교인 명부 엑셀 다운로드 로직
    public void exportMembersToExcel(HttpServletResponse response) throws IOException {
        List<Member> members = memberRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("교인 명부");

        // 헤더 생성
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("교인 ID");
        headerRow.createCell(1).setCellValue("이름");
        headerRow.createCell(2).setCellValue("생년월일");
        headerRow.createCell(3).setCellValue("전화번호");

        // 데이터 채우기
        int rowNum = 1;
        for (Member member : members) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(member.getMemberId());
            row.createCell(1).setCellValue(member.getName());
            row.createCell(2).setCellValue(member.getBirthDate());
            row.createCell(3).setCellValue(member.getPhone());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"members.xlsx\"");
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    // 2. 헌금 내역 엑셀 다운로드 (교인 이름 추가 & 성능 최적화)
    public void exportDonationsToExcel(HttpServletResponse response) throws IOException {
        List<Donation> donations = donationRepository.findAll();
        List<Member> members = memberRepository.findAll(); // 교인 전체 목록 조회

        // 💡 성능 최적화: N+1 쿼리 방지를 위해 교인 데이터를 Map으로 변환 (memberId -> name)
        // for문 안에서 매번 DB를 조회하지 않고, 메모리에 올려둔 Map에서 이름만 쏙쏙 빼옵니다.
        java.util.Map<String, String> memberNameMap = members.stream()
                .collect(java.util.stream.Collectors.toMap(
                        Member::getMemberId,
                        Member::getName,
                        (existing, replacement) -> existing // 중복 키 방지
                ));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("헌금 내역");

        // 헤더 컬럼 추가 및 순서 조정
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("헌금 ID");
        headerRow.createCell(1).setCellValue("교인 ID");
        headerRow.createCell(2).setCellValue("교인 이름"); // 👈 이름 컬럼 추가!
        headerRow.createCell(3).setCellValue("헌금 종류");
        headerRow.createCell(4).setCellValue("금액");
        headerRow.createCell(5).setCellValue("헌금 일자");

        int rowNum = 1;
        for (Donation donation : donations) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(donation.getId());
            row.createCell(1).setCellValue(donation.getMemberId());

            // 👈 Map에서 ID를 키값으로 이름을 빠르게 찾아옵니다. (혹시 교인이 삭제되었을 경우 '알 수 없음' 처리)
            String memberName = memberNameMap.getOrDefault(donation.getMemberId(), "알 수 없음");
            row.createCell(2).setCellValue(memberName);

            row.createCell(3).setCellValue(donation.getDonationType());
            row.createCell(4).setCellValue(donation.getAmount());
            row.createCell(5).setCellValue(donation.getDonationDate());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"donations.xlsx\"");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}