package com.practice.deploypractice.domain;

import java.time.LocalDateTime;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
// JPA Entity 클래스들이 BaseTimeEntity를 상속할 경우 필드들도 컬럼으로 인식하도록 설정.
// 해당 클래스는 테이블로 생성 x.
@EntityListeners(AuditingEntityListener.class)
// 해당 클래스에 Auditing 기능을 포함.
public abstract class BaseTimeEntity {

    @CreatedDate    // Entity가 생성되어 저장될 때 시간을 자동 저장.
    private LocalDateTime createdDate;

    @LastModifiedDate   // 조회한 Entity의 값이 변경될 때 시간을 자동 저장.
    private LocalDateTime modifiedDate;
}
