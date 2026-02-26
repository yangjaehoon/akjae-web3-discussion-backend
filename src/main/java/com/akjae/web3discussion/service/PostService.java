package com.akjae.web3discussion.service;

import com.akjae.web3discussion.domain.*;
import com.akjae.web3discussion.dto.request.PostRequest;
import com.akjae.web3discussion.dto.response.PostResponse;
import com.akjae.web3discussion.exception.ResourceNotFoundException;
import com.akjae.web3discussion.exception.UnauthorizedException;
import com.akjae.web3discussion.repository.*;
import com.akjae.web3discussion.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public Page<PostResponse> getPosts(Long categoryId, Long projectId, String keyword, Pageable pageable) {
        return postRepository.findWithFilters(categoryId, projectId, keyword, pageable)
                .map(PostResponse::from);
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("글을 찾을 수 없습니다: " + id));
        post.incrementViewCount();
        postRepository.save(post);
        return PostResponse.from(post);
    }

    @Transactional
    public PostResponse createPost(PostRequest request, UserPrincipal currentUser) {
        User author = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));

        Post.PostBuilder builder = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author);

        if (request.getProjectId() != null) {
            Project project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("프로젝트를 찾을 수 없습니다: " + request.getProjectId()));
            builder.project(project);
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("카테고리를 찾을 수 없습니다: " + request.getCategoryId()));
            builder.category(category);
        }

        Post post = builder.build();

        if (request.getTagNames() != null && !request.getTagNames().isEmpty()) {
            List<Tag> tags = getOrCreateTags(request.getTagNames());
            post.setTags(tags);
        }

        return PostResponse.from(postRepository.save(post));
    }

    @Transactional
    public PostResponse updatePost(Long id, PostRequest request, UserPrincipal currentUser) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("글을 찾을 수 없습니다: " + id));

        if (!post.getAuthor().getId().equals(currentUser.getId())
                && !currentUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new UnauthorizedException("글을 수정할 권한이 없습니다");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        if (request.getProjectId() != null) {
            Project project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("프로젝트를 찾을 수 없습니다"));
            post.setProject(project);
        } else {
            post.setProject(null);
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("카테고리를 찾을 수 없습니다"));
            post.setCategory(category);
        } else {
            post.setCategory(null);
        }

        if (request.getTagNames() != null) {
            post.setTags(getOrCreateTags(request.getTagNames()));
        }

        return PostResponse.from(postRepository.save(post));
    }

    @Transactional
    public void deletePost(Long id, UserPrincipal currentUser) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("글을 찾을 수 없습니다: " + id));

        if (!post.getAuthor().getId().equals(currentUser.getId())
                && !currentUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new UnauthorizedException("글을 삭제할 권한이 없습니다");
        }

        post.setStatus(Post.PostStatus.DELETED);
        postRepository.save(post);
    }

    private List<Tag> getOrCreateTags(List<String> tagNames) {
        List<Tag> tags = new ArrayList<>();
        for (String name : tagNames) {
            String trimmed = name.trim().toLowerCase();
            Tag tag = tagRepository.findByName(trimmed).orElseGet(() -> {
                Tag newTag = Tag.builder()
                        .name(trimmed)
                        .slug(trimmed.replaceAll("\\s+", "-"))
                        .build();
                return tagRepository.save(newTag);
            });
            tags.add(tag);
        }
        return tags;
    }
}
