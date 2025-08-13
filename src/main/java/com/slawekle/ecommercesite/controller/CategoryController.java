package com.slawekle.ecommercesite.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slawekle.ecommercesite.model.Category;
import com.slawekle.ecommercesite.response.ApiResponse;
import com.slawekle.ecommercesite.service.category.ICategoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final ICategoryService iCategoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
            List<Category> categories = iCategoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Categories fetched successfully", categories));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
            Category createdCategory = iCategoryService.addCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Category added successfully", createdCategory));
    }

     @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
            Category category = iCategoryService.findCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Category fetched successfully", category));
    }

    @GetMapping("/category/{name}/name")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
            Category category = iCategoryService.findCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Category fetched successfully", category));
    }

    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody Category category, @PathVariable Long id) {
            Category updatedCategory = iCategoryService.updateCategory(category, id);
            return ResponseEntity.ok(new ApiResponse("Category updated successfully", updatedCategory));
    }

    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
            iCategoryService.deleteCategory(id);
            return ResponseEntity.ok(new ApiResponse("Category deleted successfully", null));
    }
}