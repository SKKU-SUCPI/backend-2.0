package com.skku.sucpi.dto.team;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTeamRequestDto {
    private String teamName;
    private List<AddTeamMemberRequestDto> members;
}
