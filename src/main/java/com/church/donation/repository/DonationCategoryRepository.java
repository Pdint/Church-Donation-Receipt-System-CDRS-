package com.church.donation.repository;

import com.church.donation.domain.DonationCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationCategoryRepository extends JpaRepository<DonationCategory, Long> {
}