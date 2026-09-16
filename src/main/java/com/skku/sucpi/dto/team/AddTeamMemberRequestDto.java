package com.skku.sucpi.dto.team;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddTeamMemberRequestDto {
    private Long userId;
    private String memberRole;
}
