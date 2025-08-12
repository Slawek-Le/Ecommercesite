package com.slawekle.ecommercesite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slawekle.ecommercesite.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByProductId(Long productId);

}
