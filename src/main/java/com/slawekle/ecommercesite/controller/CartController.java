package com.slawekle.ecommercesite.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slawekle.ecommercesite.model.Cart;
import com.slawekle.ecommercesite.response.ApiResponse;
import com.slawekle.ecommercesite.service.cart.ICartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/carts")
public class CartController {
    private final ICartService cartService;

    @GetMapping("/user/{userId}/cart")
    public ResponseEntity<ApiResponse> getCartByUserId(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(new ApiResponse("Cart fetched successfully", cart));
    }

    @DeleteMapping("/cart/{cartId}/clear")
    public void clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
    }

}
