package com.akjae.web3discussion.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostRequest {

    @NotBlank(message = "제목은 필수입니다")
    @Size(min = 2, max = 300, message = "제목은 2~300자여야 합니다")
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    @Size(min = 10, message = "내용은 10자 이상이어야 합니다")
    private String content;

    private Long projectId;

    private Long categoryId;

    private List<String> tagNames;
}
