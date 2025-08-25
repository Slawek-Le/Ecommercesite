package com.slawekle.ecommercesite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slawekle.ecommercesite.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User findByEmail(String email);

}
