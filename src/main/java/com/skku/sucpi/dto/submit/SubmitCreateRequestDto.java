package com.skku.sucpi.dto.submit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitCreateRequestDto {
    @NotNull(message = "Activity ID is required.")
    private Long activityId;

    @NotBlank(message = "Title is required.")
    private String title;

    @NotBlank(message = "Content is required.")
    private String content;
}
