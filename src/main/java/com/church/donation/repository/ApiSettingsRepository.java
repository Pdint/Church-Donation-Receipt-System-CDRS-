package com.church.donation.repository;

import com.church.donation.domain.ApiSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiSettingsRepository extends JpaRepository<ApiSettings, Long> {
}