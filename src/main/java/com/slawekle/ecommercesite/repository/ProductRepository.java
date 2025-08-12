package com.slawekle.ecommercesite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.slawekle.ecommercesite.model.Category;
import com.slawekle.ecommercesite.model.Product;


public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findProductsByCategoryAndBrand(Category category, String brand);
    List<Product> findProductsByCategory(Category category);

    List<Product> findProductsByBrandAndName(String brand, String name);

    List<Product> findProductsByBrand(String brand);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findProductsByName(String name);
    
    boolean existsByNameAndBrand(String name, String brand);
}
