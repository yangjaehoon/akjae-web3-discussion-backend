package com.akjae.web3discussion.controller;

import com.akjae.web3discussion.dto.request.PostRequest;
import com.akjae.web3discussion.dto.response.ApiResponse;
import com.akjae.web3discussion.dto.response.PostResponse;
import com.akjae.web3discussion.security.UserPrincipal;
import com.akjae.web3discussion.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 글 목록 조회 (카테고리, 프로젝트, 키워드 필터)
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getPosts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(postService.getPosts(categoryId, projectId, keyword, pageable)));
    }

    // 글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(postService.getPost(id)));
    }

    // 글 작성 (로그인 필요)
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @Valid @RequestBody PostRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(ApiResponse.ok("글이 작성되었습니다", postService.createPost(request, currentUser)));
    }

    // 글 수정 (작성자 또는 관리자)
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(ApiResponse.ok("글이 수정되었습니다", postService.updatePost(id, request, currentUser)));
    }

    // 글 삭제 (작성자 또는 관리자, 소프트 삭제)
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        postService.deletePost(id, currentUser);
        return ResponseEntity.ok(ApiResponse.ok("글이 삭제되었습니다", null));
    }
}
