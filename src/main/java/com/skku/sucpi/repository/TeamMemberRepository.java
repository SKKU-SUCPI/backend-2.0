package com.skku.sucpi.repository;

import com.skku.sucpi.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, TeamMember.TeamMemberId> {
    Optional<TeamMember> findByTeamIdAndUserId(Long teamId, Long userId);
}
