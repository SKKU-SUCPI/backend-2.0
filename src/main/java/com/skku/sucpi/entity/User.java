package com.skku.sucpi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//@Getter
//@NoArgsConstructor
//@Entity(name = "users")
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENT
//    @Column(name = "user_id")
//    private Long id;
//
//    @Column(name = "user_name", nullable = false, length = 100)
//    private String name;
//
//    @Column(name = "user_role", length = 50)
//    private String role = "student"; // 기본값: "student"
//
//    @Column(name = "user_hakbun", length = 20)
//    private String hakbun;
//
//    @Column(name = "user_hakgwa_cd")
//    private Float hakgwaCd; // MySQL FLOAT -> Java Float
//
//    @Column(name = "user_year")
//    private Double year; // MySQL DOUBLE PRECISION -> Java Double
//
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt = LocalDateTime.now();
//
//    @PreUpdate
//    public void preUpdate() {
//        this.updatedAt = LocalDateTime.now();
//    }
//}
