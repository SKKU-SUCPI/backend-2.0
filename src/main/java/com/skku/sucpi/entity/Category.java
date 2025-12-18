package com.skku.sucpi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category")
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

    @Column(name = "category_score_sum_m") // 명륜 점수 합
    private Double sumM;

    @Column(name = "category_score_sum_y") // 율전 점수 합
    private Double sumY;

    @Column(name = "category_score_square_sum_m") // 명륜 점수 제곱 합
    private Double squareSumM;

    @Column(name = "category_score_square_sum_y") // 율전 점수 제곱 합
    private Double squareSumY;

    @Column(name = "category_count_m") // 명륜 제출 수
    private Integer countM;

    @Column(name = "category_count_y") // 율전 제출 수
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
