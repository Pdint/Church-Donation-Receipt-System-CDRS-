package com.church.donation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@MappedSuperclass // 부모 클래스로 쓸 수 있게 해줌
@Getter
@EntityListeners(AuditingEntityListener.class) // 스프링이 감시하다가 시간을 넣어줌
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}