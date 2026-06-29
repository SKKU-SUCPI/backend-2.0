package com.skku.sucpi.dto.project;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectActivityRuleResponseDto {
    private Long activityId;
    private String activityName;
    private Double customWeight;
}
