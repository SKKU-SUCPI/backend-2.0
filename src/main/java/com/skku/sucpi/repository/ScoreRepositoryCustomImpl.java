package com.skku.sucpi.repository;

import com.skku.sucpi.dto.score.MonthlyScoreDto;

import java.util.List;

public class ScoreRepositoryCustomImpl implements ScoreRepositoryCustom{
    @Override
    public List<MonthlyScoreDto> searchStudentMonthlyScore(Long userId) {
        return List.of();
    }
}
