package com.skku.sucpi.service.team;

import com.skku.sucpi.dto.score.TScoreDto;
import com.skku.sucpi.dto.team.*;
import com.skku.sucpi.entity.*;
import com.skku.sucpi.repository.*;
import com.skku.sucpi.service.score.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ScoreRepository scoreRepository;
    private final ScoreService scoreService;

    public List<TeamResponseDto> getTeamsByProjectId(Long projectId) {
        return teamRepository.findByProjectId(projectId).stream()
                .map(team -> new TeamResponseDto(team.getId(), team.getTeamName()))
                .toList();
    }

    public TeamRosterResponseDto getTeamRoster(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        List<TeamRosterMemberDto> members = team.getMembers().stream()
                .map(m -> {
                    User user = m.getUser();

                    // 1. 일반 3Q 점수 가져오기 (DB에서 직접 조회)
                    Score score = scoreRepository.findByUserId(user.getId()).orElse(null);
                    Double lq = (score != null && score.getLqScore() != null) ? score.getLqScore() : 0.0;
                    Double rq = (score != null && score.getRqScore() != null) ? score.getRqScore() : 0.0;
                    Double cq = (score != null && score.getCqScore() != null) ? score.getCqScore() : 0.0;

                    // 2. T-Score 가져오기 (기존 ScoreService 활용)
                    TScoreDto tScore = scoreService.getTScoreByUserId(user.getId());
                    Double tlq = (tScore != null && tScore.getTLq() != null) ? tScore.getTLq() : 0.0;
                    Double trq = (tScore != null && tScore.getTRq() != null) ? tScore.getTRq() : 0.0;
                    Double tcq = (tScore != null && tScore.getTCq() != null) ? tScore.getTCq() : 0.0;

                    // 3. Builder를 통해 데이터 조립
                    return TeamRosterMemberDto.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .department(user.getHakgwaCd() != null ? String.valueOf(user.getHakgwaCd()) : "미상")
                            .studentId(user.getHakbun())
                            .lq(lq)
                            .rq(rq)
                            .cq(cq)
                            .totalScore(lq + rq + cq)
                            .tlq(tlq)
                            .trq(trq)
                            .tcq(tcq)
                            .memberRole(m.getMemberRole().name())
                            .joinStatus(m.getJoinStatus())
                            .build();
                }).toList();

        return new TeamRosterResponseDto(team.getId(), team.getTeamName(), members);
    }

    @Transactional
    public void UpdateTeamJoinStatus(Long userId, PatchJoinStatusRequestDto dto) {
        TeamMember member = teamMemberRepository.findByTeamIdAndUserId(dto.getTeamId(), userId)
                .orElseThrow(() -> new IllegalArgumentException("Invitation mapping not found for user"));

        member.setJoinStatus(dto.getJoinStatus());
        teamMemberRepository.save(member);
    }

    @Transactional
    public Long createTeam(CreateTeamRequestDto dto) {
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        Team team = Team.builder()
                .teamName(dto.getTeamName())
                .project(project)
                .build();

        Team savedTeam = teamRepository.save(team);
        return savedTeam.getId();
    }

    @Transactional
    public void addMemberToTeam(Long teamId, AddTeamMemberRequestDto dto) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .user(user)
                .memberRole(MemberRole.valueOf(dto.getMemberRole()))
                .joinStatus(0)
                .build();

        teamMemberRepository.save(teamMember);
    }

    @Transactional
    public void updateTeam(Long teamId, UpdateTeamRequestDto dto) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        team.setTeamName(dto.getTeamName());

        java.util.Map<Long, String> incomingMap = new java.util.HashMap<>();
        for (AddTeamMemberRequestDto m : dto.getMembers()) {
            incomingMap.put(m.getUserId().longValue(), m.getMemberRole());
        }

        team.getMembers().removeIf(existingMember -> {
            Long existingUserId = existingMember.getUser().getId().longValue();
            return !incomingMap.containsKey(existingUserId);
        });

        for (java.util.Map.Entry<Long, String> entry : incomingMap.entrySet()) {
            Long userId = entry.getKey();
            String role = entry.getValue();

            TeamMember existingMember = team.getMembers().stream()
                    .filter(m -> m.getUser().getId().longValue() == userId.longValue())
                    .findFirst()
                    .orElse(null);

            if (existingMember != null) {
                existingMember.setMemberRole(MemberRole.valueOf(role));
            } else {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

                TeamMember newMember = TeamMember.builder()
                        .team(team)
                        .user(user)
                        .memberRole(MemberRole.valueOf(role))
                        .joinStatus(0)
                        .build();

                team.getMembers().add(newMember);
            }
        }
    }

    @Transactional
    public void deleteTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        teamRepository.delete(team);
    }
}
