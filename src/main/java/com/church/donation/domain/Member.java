package com.church.donation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter; // 👈 이거 하나면 수많은 set 메서드가 자동 생성됩니다!
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter // 👈 추가 완료!
@NoArgsConstructor
public class Member extends BaseTimeEntity {

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