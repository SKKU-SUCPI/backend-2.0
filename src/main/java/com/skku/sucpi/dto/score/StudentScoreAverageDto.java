package com.skku.sucpi.dto.score;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "StudentScoreAverageDto")
public class StudentScoreAverageDto {
    private ScoreAverageDto student;
    private ScoreAverageDto department;
    private ScoreAverageDto total;
}
