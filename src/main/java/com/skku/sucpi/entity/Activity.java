package com.skku.sucpi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "activity")
@Entity(name = "activity")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "activity_class")
    private String activityClass;

    @Column(name = "activity_detail")
    private String detail;

    @Column(name = "activity_weight")
    private Double weight;

    @Column(name = "activity_domain")
    private Long domain;
}
