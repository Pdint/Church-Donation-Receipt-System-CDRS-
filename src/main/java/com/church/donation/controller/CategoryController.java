package com.church.donation.controller;

import com.church.donation.domain.DonationCategory;
import com.church.donation.repository.DonationCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final DonationCategoryRepository categoryRepository;

    // 1. 드롭다운용 전체 목록 불러오기
    @GetMapping("/all")
    public ResponseEntity<List<DonationCategory>> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    // 2. 새로운 헌금 분류 추가하기 (설정 화면용)
    @PostMapping("/add")
    public ResponseEntity<String> addCategory(@RequestBody DonationCategory category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("분류명을 입력해주세요.");
        }
        categoryRepository.save(category);
        return ResponseEntity.ok("추가되었습니다.");
    }

    // 3. 헌금 분류 삭제하기 (설정 화면용)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.ok("삭제되었습니다.");
    }
}