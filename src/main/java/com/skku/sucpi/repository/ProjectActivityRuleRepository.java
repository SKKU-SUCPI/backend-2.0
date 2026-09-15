package com.skku.sucpi.repository;

import com.skku.sucpi.entity.ProjectActivityRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ProjectActivityRuleRepository extends JpaRepository<ProjectActivityRule, ProjectActivityRule.ProjectActivityRuleId> {
    List<ProjectActivityRule> findByProjectId(Long projectId);
    Optional<ProjectActivityRule> findByProjectIdAndActivityId(Long projectId, Long activityId);

    @Modifying
    @Query(value = "DELETE FROM project_activity_rule par WHERE project.id = :projectId")
    void deleteByProjectId(@Param("projectId") Long projectId);
}
