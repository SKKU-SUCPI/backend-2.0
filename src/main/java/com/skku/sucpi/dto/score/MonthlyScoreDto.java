package com.skku.sucpi.dto.score;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "MonthlyScoreDto")
public class MonthlyScoreDto {

    private String month;
    private Double lq;
    private Double rq;
    private Double cq;
}
