package com.skku.sucpi.repository;

import com.skku.sucpi.dto.score.ScoreAverageDto;
import com.skku.sucpi.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}

