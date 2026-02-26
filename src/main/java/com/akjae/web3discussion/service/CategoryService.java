package com.akjae.web3discussion.service;

import com.akjae.web3discussion.domain.Category;
import com.akjae.web3discussion.dto.response.CategoryResponse;
import com.akjae.web3discussion.exception.DuplicateResourceException;
import com.akjae.web3discussion.exception.ResourceNotFoundException;
import com.akjae.web3discussion.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategory(Long id) {
        return CategoryResponse.from(categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("카테고리를 찾을 수 없습니다: " + id)));
    }

    @Transactional
    public CategoryResponse createCategory(String name, String description) {
        if (categoryRepository.existsByName(name)) {
            throw new DuplicateResourceException("이미 존재하는 카테고리입니다: " + name);
        }

        Category category = Category.builder()
                .name(name)
                .slug(name.toLowerCase().replaceAll("\\s+", "-"))
                .description(description)
                .build();

        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("카테고리를 찾을 수 없습니다: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
