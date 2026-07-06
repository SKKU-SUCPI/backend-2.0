package com.skku.sucpi.dto.team;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamRosterMemberDto {
    private Long userId;
    private String name;
    private String role;
    private Integer joinStatus;
}
