package com.skku.sucpi.dto.team;

import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamRosterResponseDto {
    private Long teamId;
    private String teamName;
    private List<TeamRosterMemberDto> members;
}
