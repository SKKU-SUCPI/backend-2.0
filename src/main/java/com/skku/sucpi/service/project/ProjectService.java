package com.skku.sucpi.service.project;

import com.skku.sucpi.dto.project.*;
import com.skku.sucpi.entity.Activity;
import com.skku.sucpi.entity.Project;
import com.skku.sucpi.entity.ProjectActivityRule;
import com.skku.sucpi.repository.ActivityRepository;
import com.skku.sucpi.repository.ProjectActivityRuleRepository;
import com.skku.sucpi.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectActivityRuleRepository projectActivityRuleRepository;
    private final ActivityRepository activityRepository;

    public List<ProjectResponseDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(p -> new ProjectResponseDto(p.getId(), p.getProjectName(), p.getMultiplier(), p.getStartDate(), p.getEndDate()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createProject(CreateProjectRequestDto dto) {
        Project project = Project.builder()
                .projectName(dto.getProjectName())
                .multiplier(dto.getMultiplier())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();

        Project savedProject = projectRepository.save(project);
        return savedProject.getId();
    }

    @Transactional
    public void patchProjectRules(ProjectActivityRulePatchDto dto) {
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + dto.getProjectId()));

        projectActivityRuleRepository.deleteByProjectId(project.getId());

        List<ProjectActivityRule> updatedRules = dto.getRules().stream().map(ruleDto -> {
            Activity activity = activityRepository.findById(ruleDto.getActivityId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Activity ID: " + ruleDto.getActivityId()));

            return ProjectActivityRule.builder()
                    .project(project)
                    .activity(activity)
                    .customWeight(ruleDto.getCustomWeight())
                    .build();
        }).collect(Collectors.toList());

        projectActivityRuleRepository.saveAll(updatedRules);
    }

    @Transactional
    public void updateProject(Long projectId, UpdateProjectRequestDto dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        if (dto.getProjectName() != null && !dto.getProjectName().isBlank()) {
            project.setProjectName(dto.getProjectName());
        }
        if (dto.getMultiplier() != null) {
            project.setMultiplier(dto.getMultiplier());
        }

        if(dto.getStartDate() != null) {
            project.setStartDate(dto.getStartDate());
        }

        if(dto.getEndDate() != null) {
            project.setEndDate(dto.getEndDate());
        }
    }

    @Transactional
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectId));

        projectActivityRuleRepository.deleteByProjectId(projectId);

        projectRepository.delete(project);
    }
}
