package com.skku.sucpi.dto.submit;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitUpdateRequestDto {

    private String title;

    private String content;
}
