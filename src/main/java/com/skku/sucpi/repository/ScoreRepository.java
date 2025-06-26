package com.skku.sucpi.repository;

import com.skku.sucpi.dto.score.ScoreAverageDto;
import com.skku.sucpi.dto.score.StudentScoreDto;
import com.skku.sucpi.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
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
}

