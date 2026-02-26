package com.akjae.web3discussion.service;

import com.akjae.web3discussion.domain.Comment;
import com.akjae.web3discussion.domain.Post;
import com.akjae.web3discussion.domain.User;
import com.akjae.web3discussion.dto.request.CommentRequest;
import com.akjae.web3discussion.dto.response.CommentResponse;
import com.akjae.web3discussion.exception.ResourceNotFoundException;
import com.akjae.web3discussion.exception.UnauthorizedException;
import com.akjae.web3discussion.repository.CommentRepository;
import com.akjae.web3discussion.repository.PostRepository;
import com.akjae.web3discussion.repository.UserRepository;
import com.akjae.web3discussion.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<CommentResponse> getComments(Long postId, Pageable pageable) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("글을 찾을 수 없습니다: " + postId);
        }
        return commentRepository.findByPostIdAndParentIsNull(postId, pageable)
                .map(CommentResponse::from);
    }

    @Transactional
    public CommentResponse createComment(Long postId, CommentRequest request, UserPrincipal currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("글을 찾을 수 없습니다: " + postId));

        User author = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));

        Comment.CommentBuilder builder = Comment.builder()
                .content(request.getContent())
                .author(author)
                .post(post);

        if (request.getParentId() != null) {
            Comment parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("부모 댓글을 찾을 수 없습니다: " + request.getParentId()));
            builder.parent(parent);
        }

        return CommentResponse.from(commentRepository.save(builder.build()));
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, CommentRequest request, UserPrincipal currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("댓글을 찾을 수 없습니다: " + commentId));

        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("댓글을 수정할 권한이 없습니다");
        }

        comment.setContent(request.getContent());
        return CommentResponse.from(commentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(Long commentId, UserPrincipal currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("댓글을 찾을 수 없습니다: " + commentId));

        if (!comment.getAuthor().getId().equals(currentUser.getId())
                && !currentUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new UnauthorizedException("댓글을 삭제할 권한이 없습니다");
        }

        commentRepository.delete(comment);
    }
}
