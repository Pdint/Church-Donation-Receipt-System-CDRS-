package com.church.donation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseTimeEntity { //  공통 등록 시간 상속

    @Id
    private String memberId; // 시스템 내부적으로 사람을 구별하는 절대 고유 Primary Key
    private String name;
    private String birthDate;
    private String phone; // 휴대폰이 없는 어르신/어린이를 위해 컬럼 Null 허용 유지

    public Member(String memberId, String name, String birthDate, String phone) {
        this.memberId = memberId;
        this.name = name;
        this.birthDate = birthDate;
        this.phone = phone;
    }
}