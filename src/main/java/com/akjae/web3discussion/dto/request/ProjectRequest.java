package com.akjae.web3discussion.dto.request;

import com.akjae.web3discussion.domain.Project.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectRequest {

    @NotBlank(message = "프로젝트 이름은 필수입니다")
    @Size(min = 1, max = 200, message = "프로젝트 이름은 1~200자여야 합니다")
    private String name;

    private String description;

    private String chain;

    private String website;

    private String twitterUrl;

    private ProjectStatus status;
}
