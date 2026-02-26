package com.akjae.web3discussion.repository;

import com.akjae.web3discussion.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 최상위 댓글 (대댓글 제외)
    Page<Comment> findByPostIdAndParentIsNull(Long postId, Pageable pageable);

    // 특정 댓글의 대댓글 목록
    List<Comment> findByParentId(Long parentId);

    long countByPostId(Long postId);
}
