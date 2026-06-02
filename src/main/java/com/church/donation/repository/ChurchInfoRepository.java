package com.church.donation.repository;

import com.church.donation.domain.ChurchInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChurchInfoRepository extends JpaRepository<ChurchInfo, Long> {
}