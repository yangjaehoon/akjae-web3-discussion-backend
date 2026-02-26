package com.akjae.web3discussion.service;

import com.akjae.web3discussion.domain.Project;
import com.akjae.web3discussion.dto.request.ProjectRequest;
import com.akjae.web3discussion.dto.response.ProjectResponse;
import com.akjae.web3discussion.exception.DuplicateResourceException;
import com.akjae.web3discussion.exception.ResourceNotFoundException;
import com.akjae.web3discussion.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public Page<ProjectResponse> getProjects(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.isBlank()) {
            return projectRepository.searchByName(keyword, pageable).map(ProjectResponse::from);
        }
        return projectRepository.findAll(pageable).map(ProjectResponse::from);
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProject(Long id) {
        return ProjectResponse.from(projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("프로젝트를 찾을 수 없습니다: " + id)));
    }

    @Transactional
    public ProjectResponse createProject(ProjectRequest request) {
        if (projectRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("이미 존재하는 프로젝트입니다: " + request.getName());
        }

        Project project = Project.builder()
                .name(request.getName())
                .slug(request.getName().toLowerCase().replaceAll("\\s+", "-"))
                .description(request.getDescription())
                .chain(request.getChain())
                .website(request.getWebsite())
                .twitterUrl(request.getTwitterUrl())
                .status(request.getStatus() != null ? request.getStatus() : Project.ProjectStatus.ACTIVE)
                .build();

        return ProjectResponse.from(projectRepository.save(project));
    }

    @Transactional
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("프로젝트를 찾을 수 없습니다: " + id));

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setChain(request.getChain());
        project.setWebsite(request.getWebsite());
        project.setTwitterUrl(request.getTwitterUrl());
        if (request.getStatus() != null) {
            project.setStatus(request.getStatus());
        }

        return ProjectResponse.from(projectRepository.save(project));
    }

    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("프로젝트를 찾을 수 없습니다: " + id);
        }
        projectRepository.deleteById(id);
    }
}
