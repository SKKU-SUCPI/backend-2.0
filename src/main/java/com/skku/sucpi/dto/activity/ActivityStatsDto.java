package com.skku.sucpi.dto.activity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class ActivityStatsDto {

    @Getter
    @Builder
    @Schema(name = "ActivityDto_SubmitCount")
    public static class SubmitCount {
        Long sw;
        Long intelligentSw;
        Long soc;
        Long total;
    }
}
