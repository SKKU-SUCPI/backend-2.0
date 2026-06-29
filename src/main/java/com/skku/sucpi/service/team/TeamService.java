package com.skku.sucpi.service.team;

import com.skku.sucpi.dto.team.*;
import com.skku.sucpi.entity.*;
import com.skku.sucpi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    public List<TeamResponseDto> getTeamsByProjectId(Long projectId) {
        return teamRepository.findByProjectId(projectId).stream()
                .map(team -> new TeamResponseDto(team.getId(), team.getTeamName()))
                .toList();
    }

    public TeamRosterResponseDto getTeamRoster(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        List<TeamRosterMemberDto> members = team.getMembers().stream()
                .map(m -> new TeamRosterMemberDto(
                        m.getUser().getId(),
                        m.getUser().getName(),
                        m.getMemberRole().name(),
                        m.getJoinStatus().name()
                )).toList();

        return new TeamRosterResponseDto(team.getId(), team.getTeamName(), members);
    }

    @Transactional
    public void UpdateTeamJoinStatus(Long userId, PatchJoinStatusRequestDto dto) {
        TeamMember member = teamMemberRepository.findByTeamIdAndUserId(dto.getTeamId(), userId)
                .orElseThrow(() -> new IllegalArgumentException("Invitation mapping not found for user"));

        member.setJoinStatus(JoinStatus.valueOf(dto.getJoinStatus()));
        teamMemberRepository.save(member);
    }
}
