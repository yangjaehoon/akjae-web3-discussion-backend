package com.akjae.web3discussion.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {

    @NotBlank(message = "댓글 내용은 필수입니다")
    @Size(min = 1, max = 2000, message = "댓글은 1~2000자여야 합니다")
    private String content;

    // 대댓글인 경우 부모 댓글 ID
    private Long parentId;
}
