package com.skku.sucpi.entity;

import com.skku.sucpi.dto.user.SSOUserDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENT
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name", nullable = false, length = 100)
    private String name;

    @Column(name = "user_role", length = 50)
    private String role = "student"; // 기본값: "student"

    @Comment("SSO에서 가져온 학번 또는 교직원번호")
    @Column(name = "user_hakbun", length = 20)
    private String hakbun;

    @Column(name = "user_hakgwa_cd")
    private Float hakgwaCd;

    @Column(name = "user_year")
    private Double year;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Submit> submits;

    @Builder
    public User(String name, String role, String hakbun, Float hakgwaCd, Double year) {
        this.name = name;
        this.role = role;
        this.hakbun = hakbun;
        this.hakgwaCd = hakgwaCd;
        this.year = year;
    }

    public User(SSOUserDto ssoUserDto) {
        this.name = ssoUserDto.getUserName();
        this.role = ssoUserDto.getRole();
        this.hakbun = ssoUserDto.getHakbun();
        this.hakgwaCd = ssoUserDto.getHakgwaCd();
        this.year = 0D;
    }
}
