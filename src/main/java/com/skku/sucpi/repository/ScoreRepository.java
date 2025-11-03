package com.skku.sucpi.repository;

import com.skku.sucpi.dto.score.ScoreAverageDto;
import com.skku.sucpi.dto.score.StudentScoreDto;
import com.skku.sucpi.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    Optional<Score> findByUserId(Long userId);

    @Query("SELECT AVG(s.lqScore) FROM score s")
    Double findAverageLqScore();

    @Query("SELECT AVG(s.rqScore) FROM score s")
    Double findAverageRqScore();

    @Query("SELECT AVG(s.cqScore) FROM score s")
    Double findAverageCqScore();

    @Query("SELECT new com.skku.sucpi.dto.score.ScoreAverageDto(AVG(s.lqScore), AVG(s.rqScore), AVG(s.cqScore)) FROM score s JOIN s.user u WHERE u.hakgwaCd = 1.0")
    ScoreAverageDto findAverageScoreOfSw();

    @Query("SELECT new com.skku.sucpi.dto.score.ScoreAverageDto(AVG(s.lqScore), AVG(s.rqScore), AVG(s.cqScore)) FROM score s JOIN s.user u WHERE u.hakgwaCd = 2.0")
    ScoreAverageDto findAverageScoreOfIntelligentSw();

    // 글로벌융합학과
    @Query("SELECT new com.skku.sucpi.dto.score.ScoreAverageDto(AVG(s.lqScore), AVG(s.rqScore), AVG(s.cqScore)) FROM score s JOIN s.user u WHERE u.hakgwaCd = 3.0")
    ScoreAverageDto findAverageScoreOfSoc();

    // 학생 본인의 lq 점수, rank, total
    @Query(value = """
            SELECT score, average, percentRank
            FROM (
                SELECT lq_score AS score,
                       (SELECT AVG(s2.lq_score) FROM score s2) AS average,
                       PERCENT_RANK() OVER (ORDER BY lq_score DESC) AS percentRank,
                       user_id
                FROM score
            ) AS ranked
            WHERE user_id = :userId;
        """, nativeQuery = true)
    StudentScoreDto.ScoreInfoInterface findStudentLqInfo(@Param("userId") Long userId);

    // 학생 본인의 rq 점수, rank, total
    @Query(value = """
            SELECT score, average, percentRank
            FROM (
                SELECT rq_score AS score,
                       (SELECT AVG(s2.rq_score) FROM score s2) AS average,
                       PERCENT_RANK() OVER (ORDER BY rq_score DESC) AS percentRank,
                       user_id
                FROM score
            ) AS ranked
            WHERE user_id = :userId;
        """, nativeQuery = true)
    StudentScoreDto.ScoreInfoInterface findStudentRqInfo(@Param("userId") Long userId);

    // 학생 본인의 cq 점수, rank, total
    @Query(value = """
            SELECT score, average, percentRank
            FROM (
                SELECT cq_score AS score,
                       (SELECT AVG(s2.cq_score) FROM score s2) AS average,
                       PERCENT_RANK() OVER (ORDER BY rq_score DESC) AS percentRank,
                       user_id
                FROM score
            ) AS ranked
            WHERE user_id = :userId;
        """, nativeQuery = true)
    StudentScoreDto.ScoreInfoInterface findStudentCqInfo(@Param("userId") Long userId);

    // 모든 학생 점수 재연산
    @Modifying(clearAutomatically = true)
    @Query(value = """
        UPDATE score s
        JOIN (
            SELECT
                u.user_id,
                SUM(CASE WHEN c.category_id = 1 THEN a.activity_weight ELSE 0 END) AS lq_score,
                SUM(CASE WHEN c.category_id = 2 THEN a.activity_weight ELSE 0 END) AS rq_score,
                SUM(CASE WHEN c.category_id = 3 THEN a.activity_weight ELSE 0 END) AS cq_score
            FROM users u
            LEFT JOIN submit sb ON u.user_id = sb.user_id AND sb.submit_state = 1
            LEFT JOIN activity a ON sb.activity_id = a.activity_id
            LEFT JOIN category c ON a.category_id = c.category_id
            GROUP BY u.user_id
        ) AS calculated ON s.user_id = calculated.user_id
        SET
            s.lq_score = calculated.lq_score,
            s.rq_score = calculated.rq_score,
            s.cq_score = calculated.cq_score;
        """, nativeQuery = true)
    int updateAllScoresBasedOnSubmits(); // 업데이트된 레코드 수를 반환
}

