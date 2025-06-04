package com.skku.sucpi.dto.score;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name ="StudentScoreDto")
public class StudentScoreDto {

    public interface ScoreInfoInterface {
        Double getScore();
        Double getAverage();
        int getRank();
        int getTotal();
    }

    @Getter
    @Builder
    @Schema(name = "StudentScore_Response")
    static public class Response {
        ScoreInfo lq;
        ScoreInfo rq;
        ScoreInfo cq;
    }

    @Getter
    @Builder
    @Schema(name = "StudentScore_ScoreInfo")
    static public class ScoreInfo {
        Double score;
        Double average;
        Double percentile;
    }
}
