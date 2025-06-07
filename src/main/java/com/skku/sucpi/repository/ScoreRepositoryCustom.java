package com.skku.sucpi.repository;

import com.skku.sucpi.dto.score.MonthlyScoreDto;

import java.util.List;

public interface ScoreRepositoryCustom {
    List<MonthlyScoreDto>  searchStudentMonthlyScore(Long userId);
}
