package com.akjae.web3discussion.controller;

import com.akjae.web3discussion.dto.request.ProjectRequest;
import com.akjae.web3discussion.dto.response.ApiResponse;
import com.akjae.web3discussion.dto.response.ProjectResponse;
import com.akjae.web3discussion.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProjectResponse>>> getProjects(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(projectService.getProjects(keyword, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProject(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(projectService.getProject(id)));
    }

    // 프로젝트 등록 (로그인 필요)
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
            @Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("프로젝트가 등록되었습니다", projectService.createProject(request)));
    }

    // 프로젝트 수정 (관리자만)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("프로젝트가 수정되었습니다", projectService.updateProject(id, request)));
    }

    // 프로젝트 삭제 (관리자만)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(ApiResponse.ok("프로젝트가 삭제되었습니다", null));
    }
}
