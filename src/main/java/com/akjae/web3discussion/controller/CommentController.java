package com.akjae.web3discussion.controller;

import com.akjae.web3discussion.dto.request.CommentRequest;
import com.akjae.web3discussion.dto.response.ApiResponse;
import com.akjae.web3discussion.dto.response.CommentResponse;
import com.akjae.web3discussion.security.UserPrincipal;
import com.akjae.web3discussion.service.CommentService;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 글의 댓글 목록 조회
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<Page<CommentResponse>>> getComments(
            @PathVariable Long postId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(commentService.getComments(postId, pageable)));
    }

    // 댓글 작성
    @PostMapping("/posts/{postId}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(ApiResponse.ok("댓글이 작성되었습니다", commentService.createComment(postId, request, currentUser)));
    }

    // 댓글 수정
    @PutMapping("/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(ApiResponse.ok("댓글이 수정되었습니다", commentService.updateComment(commentId, request, currentUser)));
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        commentService.deleteComment(commentId, currentUser);
        return ResponseEntity.ok(ApiResponse.ok("댓글이 삭제되었습니다", null));
    }
}
