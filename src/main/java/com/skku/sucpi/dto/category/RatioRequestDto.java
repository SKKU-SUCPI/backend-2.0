package com.skku.sucpi.dto.category;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RatioRequestDto {

    @NotNull(message = "LQ value is required.")
    private Double lq;
    @NotNull(message = "RQ value is required.")
    private Double rq;
    @NotNull(message = "CQ value is required.")
    private Double cq;
}
