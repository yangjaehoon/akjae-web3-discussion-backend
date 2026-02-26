package com.akjae.web3discussion.repository;

import com.akjae.web3discussion.domain.Post;
import com.akjae.web3discussion.domain.Post.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByStatus(PostStatus status, Pageable pageable);

    Page<Post> findByCategoryIdAndStatus(Long categoryId, PostStatus status, Pageable pageable);

    Page<Post> findByProjectIdAndStatus(Long projectId, PostStatus status, Pageable pageable);

    Page<Post> findByAuthorIdAndStatus(Long authorId, PostStatus status, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.status = 'PUBLISHED' AND " +
           "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Post> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.id = :tagId AND p.status = 'PUBLISHED'")
    Page<Post> findByTagId(@Param("tagId") Long tagId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.status = 'PUBLISHED' AND " +
           "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
           "(:projectId IS NULL OR p.project.id = :projectId) AND " +
           "(:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Post> findWithFilters(
            @Param("categoryId") Long categoryId,
            @Param("projectId") Long projectId,
            @Param("keyword") String keyword,
            Pageable pageable);
}
