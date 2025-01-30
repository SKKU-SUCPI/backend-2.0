package com.skku.sucpi.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * setter 사용 금지
 * 필드 수정은 메소드를 통해 명시적으로 수정
 */
@Getter
@NoArgsConstructor
@Entity
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @Builder
    public TestEntity(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    // 수정 메소드
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
