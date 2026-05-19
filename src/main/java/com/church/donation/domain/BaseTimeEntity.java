package com.church.donation.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass // 자식 클래스에게 이 필드들을 물려줌
@EntityListeners(AuditingEntityListener.class) // 시간에 맞춰 자동으로 값을 넣어주겠다는 뜻
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt; // 👈 여기에 딱 한 번만 적습니다!

    //수정 시간 추가
    @LastModifiedDate
    private LocalDateTime updatedAt;
}