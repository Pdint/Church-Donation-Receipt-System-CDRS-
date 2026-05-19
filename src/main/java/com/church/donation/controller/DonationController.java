package com.church.donation.controller;

import com.church.donation.domain.Donation;
import com.church.donation.dto.DonationRequest;
import com.church.donation.repository.DonationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

    @RestController
    @RequestMapping("/api/donation")
    public class DonationController {

        private final DonationRepository donationRepository;

        public DonationController(DonationRepository donationRepository) {
            this.donationRepository = donationRepository;
        }

        @PostMapping("/batch")
        public String saveBatch(@RequestBody List<DonationRequest> requests) {
            // Request DTO 리스트를 Entity 리스트로 변환
            List<Donation> donations = requests.stream()
                    .map(req -> new Donation(
                            req.getMemberId(),
                            req.getAmount(),
                            req.getDonationDate()
                    ))
                    .collect(Collectors.toList());

            // 한 번에 DB 저장 (Bulk Save)
            donationRepository.saveAll(donations);
            return "SUCCESS";
        }
    }
