package com.skku.sucpi.repository;

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
}

