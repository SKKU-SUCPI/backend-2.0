package com.skku.sucpi.dto.submit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

public class SubmitCountDto {

    @Getter
    @Jacksonized
    @Builder
    public static class Count {
        Long currentMonth;
        Long lastMonth;
        Long total;
    }

    @Getter
    @Jacksonized
    @Builder
    @Schema(name = "SubmitCountDto_Response")
    public static class Response {
        Count lq;
        Count rq;
        Count cq;
    }
}
