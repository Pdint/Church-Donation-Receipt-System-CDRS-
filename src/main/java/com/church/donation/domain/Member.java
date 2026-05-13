package com.church.donation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id
    private String memberId;
    private String name;
    private String birthDate;
    private String phone;

    public Member(String memberId, String name, String birthDate, String phone) {
        this.memberId = memberId;
        this.name = name;
        this.birthDate = birthDate;
        this.phone = phone;
    }
}