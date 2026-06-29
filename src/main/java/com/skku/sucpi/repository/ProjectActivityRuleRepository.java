package com.skku.sucpi.repository;

import com.skku.sucpi.entity.ProjectActivityRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProjectActivityRuleRepository extends JpaRepository<ProjectActivityRule, Long> {
    List<ProjectActivityRule> findByProjectId(Long projectId);
    Optional<ProjectActivityRule> findByProjectIdAndActivityId(Long projectId, Long activityId);
}
