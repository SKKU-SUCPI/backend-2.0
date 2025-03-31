package com.skku.sucpi.dto.score;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@Builder
public class TScoreDto {

    private Double tLq;
    private Double tCq;
    private Double tRq;
}
