package com.church.donation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
//@EntityListeners(AuditingEntityListener.class) // 엔티티가 영속성 컨텍스트에 적재될 때 시간을 자동으로 매핑
public abstract class BaseTimeEntity {

    @CreationTimestamp
    @Column(updatable = false) // 👈 데이터가 등록된 이후에는 절대 수정(UPDATE)할 수 없도록 강제 제어
    private LocalDateTime createdAt;
}