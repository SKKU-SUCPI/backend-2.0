package com.skku.sucpi.dto.category;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@Builder
public class RatioResponseDto {

    private final Double lq;
    private final Double rq;
    private final Double cq;
}
