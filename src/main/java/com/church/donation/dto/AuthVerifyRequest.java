package com.church.donation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class AuthVerifyRequest {
    private String name;
    private String birthDate;
    private String phone;
    private String code;
}