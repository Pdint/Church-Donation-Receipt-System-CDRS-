package com.church.donation.controller;

import com.church.donation.service.ExcelService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelService excelService;

    // HTML의 <a href="/admin/members/excel"> 버튼을 받아주는 창구입니다.
    @GetMapping("/admin/members/excel")
    public void downloadMembersExcel(HttpServletResponse response) throws IOException {
        // Service에게 엑셀 파일을 만들어서 브라우저(response)로 쏘라고 지시합니다.
        excelService.exportMembersToExcel(response);
    }

    //  작성해두신 '헌금 내역 엑셀 다운로드' 기능도 나중에 쓰실 수 있게 미리 뚫어둡니다!
    @GetMapping("/admin/donations/excel")
    public void downloadDonationsExcel(HttpServletResponse response) throws IOException {
        excelService.exportDonationsToExcel(response);
    }
}