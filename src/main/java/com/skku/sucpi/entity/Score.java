package com.skku.sucpi.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "score")
@Entity(name = "score")
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "lq_score")
    private Double lqScore;

    @Column(name = "rq_score")
    private Double rqScore;

    @Column(name = "cq_score")
    private Double cqScore;

    public void updateScore(Long categoryId, Double diff) {
        if (categoryId == 1) {
            this.lqScore += diff;
        } else if (categoryId ==2) {
            this.rqScore += diff;
        } else if (categoryId == 3) {
            this.cqScore += diff;
        } else {
            throw new IllegalArgumentException("lq, rq, cq 가 모두 아닙니다.");
        }
    }

    public Score(User user) {
        this.lqScore = 0D;
        this.rqScore = 0D;
        this.cqScore = 0D;
        this.user = user;
    }
}
