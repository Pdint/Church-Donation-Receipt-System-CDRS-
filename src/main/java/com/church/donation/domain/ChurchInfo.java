package com.church.donation.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChurchInfo {

    // 항상 1번 데이터만 쓸 것이므로 Id 자동생성을 뺐습니다.
    @Id
    private Long id = 1L;

    private String name;        // 교회명
    private String repName;     // 대표자명 (담임목사)
    private String businessNo;  // 고유번호 (사업자등록번호)
    private String address;     // 교회 주소
    private String serialNumber;// 일련 번호

    @Builder
    public ChurchInfo(String name, String repName, String businessNo, String address, String serialNumber) {
        this.id = 1L; // 무조건 1번으로 고정!
        this.name = name;
        this.repName = repName;
        this.businessNo = businessNo;
        this.address = address;
        this.serialNumber = serialNumber;
    }

    // 덮어쓰기(수정)를 위한 메서드
    public void updateInfo(String name, String repName, String businessNo, String address, String serialNumber) {
        this.name = name;
        this.repName = repName;
        this.businessNo = businessNo;
        this.address = address;
        this.serialNumber = serialNumber;
    }
}
