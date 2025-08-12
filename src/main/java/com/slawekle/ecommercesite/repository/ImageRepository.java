package com.slawekle.ecommercesite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slawekle.ecommercesite.model.Image;


public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findImageByProductId(Long productId);
}
