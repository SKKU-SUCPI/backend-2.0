package com.skku.sucpi.dto.score;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@Builder
@Schema(name = "ScoreDepartmentAverageDto")
public class ScoreDepartmentAverageDto {

    private ScoreAverageDto sw;
    private ScoreAverageDto intelligentSw;
    private ScoreAverageDto soc;
}
