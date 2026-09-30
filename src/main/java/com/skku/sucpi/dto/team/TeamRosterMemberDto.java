package com.skku.sucpi.dto.team;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamRosterMemberDto {
    private Long id;
    private String name;
    private String department;
    private String studentId;

    private Double lq;
    private Double rq;
    private Double cq;
    private Double totalScore;

    private Double tlq;
    private Double trq;
    private Double tcq;

    private String memberRole;
    private Integer joinStatus;
}
