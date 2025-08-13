package com.slawekle.ecommercesite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slawekle.ecommercesite.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUserId(Long userId);

}
