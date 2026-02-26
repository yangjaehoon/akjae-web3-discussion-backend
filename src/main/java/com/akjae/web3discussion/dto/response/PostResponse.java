package com.akjae.web3discussion.dto.response;

import com.akjae.web3discussion.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private String authorUsername;
    private Long authorId;
    private ProjectInfo project;
    private CategoryInfo category;
    private List<String> tags;
    private int viewCount;
    private int likeCount;
    private long commentCount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorUsername(post.getAuthor().getUsername())
                .authorId(post.getAuthor().getId())
                .project(post.getProject() != null ? ProjectInfo.from(post.getProject()) : null)
                .category(post.getCategory() != null ? CategoryInfo.from(post.getCategory()) : null)
                .tags(post.getTags().stream().map(t -> t.getName()).collect(Collectors.toList()))
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .commentCount(post.getComments().size())
                .status(post.getStatus().name())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    @Getter
    @Builder
    public static class ProjectInfo {
        private Long id;
        private String name;
        private String slug;

        public static ProjectInfo from(com.akjae.web3discussion.domain.Project project) {
            return ProjectInfo.builder()
                    .id(project.getId())
                    .name(project.getName())
                    .slug(project.getSlug())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CategoryInfo {
        private Long id;
        private String name;
        private String slug;

        public static CategoryInfo from(com.akjae.web3discussion.domain.Category category) {
            return CategoryInfo.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .slug(category.getSlug())
                    .build();
        }
    }
}
