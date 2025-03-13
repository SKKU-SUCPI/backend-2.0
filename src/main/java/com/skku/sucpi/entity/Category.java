package com.skku.sucpi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name")
    private String name;

    @Column(name = "category_ratio")
    private Double ratio;

    @Column(name = "category_score_sum")
    private Double sum;

    @Column(name = "category_score_deviation")
    private Double deviation;

    public void updateRatio(Double ratio) {
        this.ratio = ratio;
    }
}
