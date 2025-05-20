package com.skku.sucpi.dto.submit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitCreateRequestDto {
    @NotNull(message = "activityId는 필수입니다.")
    private Long activityId;

    @NotBlank(message = "content는 필수입니다.")
    private String content;
}
