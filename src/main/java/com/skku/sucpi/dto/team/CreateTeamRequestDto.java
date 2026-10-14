package com.skku.sucpi.dto.team;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTeamRequestDto {
    private Long projectId;
    private String teamName;
}
