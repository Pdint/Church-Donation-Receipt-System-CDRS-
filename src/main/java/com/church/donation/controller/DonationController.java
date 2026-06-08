package com.church.donation.controller;

import com.church.donation.dto.DonationRequest;
import com.church.donation.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;


    @PostMapping("/batch")
    public ResponseEntity<String> saveDonationsBatch(@RequestBody List<DonationRequest> requests) {
        // 비즈니스 로직 위임
        donationService.saveBulkDonations(requests);
        return ResponseEntity.ok("OK");
    }
}