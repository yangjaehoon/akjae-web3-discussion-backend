package com.akjae.web3discussion.controller;

import com.akjae.web3discussion.dto.response.ApiResponse;
import com.akjae.web3discussion.dto.response.CategoryResponse;
import com.akjae.web3discussion.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        return ResponseEntity.ok(ApiResponse.ok(categoryService.getAllCategories()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(categoryService.getCategory(id)));
    }

    // 카테고리 생성 (관리자만)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @RequestBody Map<String, String> body) {
        String name = body.get("name");
        String description = body.get("description");
        return ResponseEntity.ok(ApiResponse.ok("카테고리가 생성되었습니다", categoryService.createCategory(name, description)));
    }

    // 카테고리 삭제 (관리자만)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.ok("카테고리가 삭제되었습니다", null));
    }
}
