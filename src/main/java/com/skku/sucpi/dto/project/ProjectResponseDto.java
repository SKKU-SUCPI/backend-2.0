package com.skku.sucpi.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectResponseDto {
    private Long projectId;
    private String projectName;
}
