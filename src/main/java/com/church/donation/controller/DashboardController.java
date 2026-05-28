package com.church.donation.controller;

import com.church.donation.dto.MonthlyStat;
import com.church.donation.repository.DonationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DonationRepository donationRepository;

    public DashboardController(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    // 👈 프론트엔드 차트에서 호출할 통계 데이터 API
    @GetMapping("/monthly")
    public List<MonthlyStat> getMonthlyStats(@RequestParam(defaultValue = "2026") int year) {
        // DB에서 2026년 월별 총합 데이터를 뽑아서 리턴
        return donationRepository.findMonthlyTotalByYear(year);
    }
}