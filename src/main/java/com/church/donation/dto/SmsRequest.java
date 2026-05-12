package com.church.donation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SmsRequest {
    private String name;
    private String phone;
}