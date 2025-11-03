package com.skku.sucpi.dto.activity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ActivityRequestDto {

    private Long categoryId;

    private Long activityId;
    private String activityClass;
    private String activityDetail;
    private Double activityWeight;
}
