package com.skku.sucpi.controller.team;

import com.skku.sucpi.dto.ApiResponse;
import com.skku.sucpi.dto.team.TeamResponseDto;
import com.skku.sucpi.dto.team.TeamRosterResponseDto;
import com.skku.sucpi.dto.team.PatchJoinStatusRequestDto;
import com.skku.sucpi.service.team.TeamService;
import com.skku.sucpi.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team")
@Tag(name = "Team API", description = "팀 및 공모전 그룹 빌딩 전용 API")
@SecurityRequirement(name = "bearerAuth")
public class TeamController {

    private final TeamService teamService;
    private final JWTUtil jwtUtil;

    @GetMapping("/project/{projectId}")
    @Operation(
            summary = "프로젝트 산하 전체 참가 팀 조회",
            description = """
                    **설명**
                    - 특정 공모전/프로젝트 스코프 내에 생성된 모든 팀 브래킷 목록 조회
                    """
    )
    public ApiResponse<List<TeamResponseDto>> getProjectTeams(
            @PathVariable Long projectId,
            HttpServletRequest request
    ) {
        return ApiResponse.success(teamService.getTeamsByProjectId(projectId), request.getRequestURI());
    }

    @GetMapping("/{teamId}/roster")
    @Operation(
            summary = "팀 로스터 상세 조회",
            description = """
                    **설명**
                    - 특정 팀에 가입된 멤버들의 인적사항, 역할(LEADER/MEMBER), 초대 상태(joinStatus) 조회
                    """
    )
    public ApiResponse<TeamRosterResponseDto> getTeamRoster(
            @PathVariable Long teamId,
            HttpServletRequest request
    ) {
        return ApiResponse.success(teamService.getTeamRoster(teamId), request.getRequestURI());
    }

    @PostMapping("/join-status/patch")
    @Operation(
            summary = "팀 가입 상태 변경 (초대 수락/거절)",
            description = """
                    **설명**
                    - 학생이 본인에게 발송된 팀 가입 초대를 수락(JOINED)하거나 거절(REJECTED)하는 트랜잭션 API
                    """
    )
    public ApiResponse<Void> patchTeamJoinStatus(
            @Valid @RequestBody PatchJoinStatusRequestDto data,
            HttpServletRequest request
    ) {
        String token = jwtUtil.parseJWT(request);
        Long userId = jwtUtil.getUserId(token);

        teamService.UpdateTeamJoinStatus(userId, data);
        return ApiResponse.success(null, request.getRequestURI());
    }
}