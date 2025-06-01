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

    @Column(name = "category_score_sum_m")
    private Double sumM;

    @Column(name = "category_score_sum_y")
    private Double sumY;

    @Column(name = "category_score_square_sum_m")
    private Double squareSumM;

    @Column(name = "category_score_square_sum_y")
    private Double squareSumY;

    @Column(name = "category_count_m")
    private Integer countM;

    @Column(name = "category_count_y")
    private Integer countY;

    public void updateRatio(Double ratio) {
        this.ratio = ratio;
    }

    public void increaseCountM() {
        this.countM++;
    }

    public void increaseCountY() {
        this.countY++;
    }

    public void updateSumAndSquareSum(Double score, boolean isYuljeon) {
        Double square = score * score;

        if (isYuljeon) {
            sumY += score;
            if (score >= 0) {
                this.squareSumY += square;
            } else {
                this.squareSumY -= square;
            }
        } else {
            sumM += score;
            if (score >= 0) {
                this.squareSumM += square;
            } else {
                this.squareSumM -= square;
            }
        }
    }
}
