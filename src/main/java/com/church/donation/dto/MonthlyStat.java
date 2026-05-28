package com.church.donation.dto;

public interface MonthlyStat {
    Integer getMonth(); // 월 (1~12)
    Long getTotal();    // 해당 월의 헌금 총합
}