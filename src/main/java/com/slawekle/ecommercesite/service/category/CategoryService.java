package com.slawekle.ecommercesite.service.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.slawekle.ecommercesite.model.Category;
import com.slawekle.ecommercesite.repository.CategoryRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c-> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(() -> new EntityExistsException("Category with name " + category.getName() + " already exists."));
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(findCategoryById(id))
            .map(existingCategory -> {
            existingCategory.setName(category.getName());
            return categoryRepository.save(existingCategory);
        }).orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found."));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete,
                        () -> {
                            throw new EntityNotFoundException("Category with id " + id + " not found.");
                        });
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found."));
    }
}
