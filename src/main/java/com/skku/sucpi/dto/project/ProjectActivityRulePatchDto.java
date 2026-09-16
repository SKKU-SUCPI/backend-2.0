package com.skku.sucpi.dto.project;

import lombok.*;
import java.util.List;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectActivityRulePatchDto {
    @NotNull
    private Long projectId;
    @NotNull
    private List<RuleRequest> rules;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RuleRequest {
        @NotNull
        private Long activityId;
        @NotNull
        private Double customWeight;
    }
}
