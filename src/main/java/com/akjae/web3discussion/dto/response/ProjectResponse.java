package com.akjae.web3discussion.dto.response;

import com.akjae.web3discussion.domain.Project;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectResponse {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private String chain;
    private String website;
    private String twitterUrl;
    private String status;
    private long postCount;

    public static ProjectResponse from(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .slug(project.getSlug())
                .description(project.getDescription())
                .chain(project.getChain())
                .website(project.getWebsite())
                .twitterUrl(project.getTwitterUrl())
                .status(project.getStatus().name())
                .postCount(project.getPosts().size())
                .build();
    }
}
