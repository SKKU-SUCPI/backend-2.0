package com.skku.sucpi.service.project;

import com.skku.sucpi.dto.project.ProjectResponseDto;
import com.skku.sucpi.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public List<ProjectResponseDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(p -> new ProjectResponseDto(p.getId(), p.getProjectName()))
                .collect(Collectors.toList());
    }
}
