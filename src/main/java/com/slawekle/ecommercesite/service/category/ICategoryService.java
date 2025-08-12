package com.slawekle.ecommercesite.service.category;

import java.util.List;

import com.slawekle.ecommercesite.model.Category;

public interface ICategoryService {
    Category addCategory(Category category);
    Category updateCategory(Category category, Long id);
    void deleteCategory(Long id);
    List<Category> getAllCategories();
    Category findCategoryByName(String name);
    Category findCategoryById(Long id);
}
