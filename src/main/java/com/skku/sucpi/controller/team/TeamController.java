package com.skku.sucpi.controller.team;

import com.skku.sucpi.dto.ApiResponse;
import com.skku.sucpi.dto.team.*;
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

    @PostMapping("")
    @Operation(
            summary = "새로운 팀 생성",
            description = """
                    **설명**
                    - 특정 프로젝트 하위에 새로운 팀 브래킷을 생성하고 발급된 Team ID를 반환합니다.
                    """
    )
    public ApiResponse<Long> createTeam(
            @RequestBody CreateTeamRequestDto request,
            HttpServletRequest httpServletRequest
    ) {
        Long newTeamId = teamService.createTeam(request);
        return ApiResponse.success(newTeamId, httpServletRequest.getRequestURI());
    }

    @PostMapping("/{teamId}/members")
    @Operation(
            summary = "팀 멤버 등록",
            description = """
                    **설명**
                    - 생성된 팀에 학생을 배정하거나 초대합니다. 관리자 등록 시 즉시 가입(joinStatus = 0) 처리됩니다.
                    """
    )
    public ApiResponse<Void> addTeamMember(
            @PathVariable Long teamId,
            @RequestBody AddTeamMemberRequestDto request,
            HttpServletRequest httpServletRequest
    ) {
        teamService.addMemberToTeam(teamId, request);
        return ApiResponse.success(null, httpServletRequest.getRequestURI());
    }

    @PutMapping("/{teamId}")
    @Operation(
            summary = "팀 수정",
            description = """
                **설명**
                - 팀의 이름과 소속 멤버 목록을 업데이트 합니다. 기존 멤버의 가입 시간은 보존됩니다.
                """
    )
    public ApiResponse<Void> updateTeam(
            @PathVariable Long teamId,
            @RequestBody UpdateTeamRequestDto request,
            HttpServletRequest httpServletRequest
            ) {
        teamService.updateTeam(teamId, request);
        return ApiResponse.success(null, httpServletRequest.getRequestURI());
    }

    @DeleteMapping("/{teamId}")
    @Operation(
            summary = "팀 삭제",
            description = """
                    **설명**
                    - 특정 팀과 팀 내 모든 소속 정보를 삭제합니다.
                    """
    )
    public ApiResponse<Void> deleteTeam(
            @PathVariable Long teamId,
            HttpServletRequest httpServletRequest
    ) {
        teamService.deleteTeam(teamId);
        return ApiResponse.success(null, httpServletRequest.getRequestURI());
    }
}