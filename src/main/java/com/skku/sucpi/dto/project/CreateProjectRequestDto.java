package com.skku.sucpi.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectRequestDto {
    @NotBlank
    private String projectName;

    private LocalDate startDate;
    private LocalDate endDate;

    @NotNull
    private Double multiplier;

    private List<ProjectActivityRulePatchDto.RuleRequest> rules;
}
