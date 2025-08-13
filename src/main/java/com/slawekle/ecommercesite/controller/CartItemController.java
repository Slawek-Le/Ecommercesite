package com.slawekle.ecommercesite.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.slawekle.ecommercesite.model.Cart;
import com.slawekle.ecommercesite.model.User;
import com.slawekle.ecommercesite.response.ApiResponse;
import com.slawekle.ecommercesite.service.cart.ICartItemService;
import com.slawekle.ecommercesite.service.cart.ICartService;
import com.slawekle.ecommercesite.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    private final ICartItemService cartItemService;
    private final ICartService cartService;
    private final UserService userService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(Long userId, @RequestParam Long productId,
            @RequestParam int quantity) {
        User user = userService.getUserById(1L);
        Cart cart = cartService.initializeCart(user);
        cartItemService.addItemToCart(cart.getId(), productId, quantity);
        return ResponseEntity.ok(new ApiResponse("Item added to cart successfully", null));
    }

    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        cartItemService.removeItemFromCart(cartId, itemId);
        return ResponseEntity.ok(new ApiResponse("Item removed from cart successfully", null));
    }

    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateCartItem(
        @PathVariable Long cartId,
        @PathVariable Long itemId,
        @RequestParam int quantity) {
        cartItemService.updateItemQuantity(cartId, itemId, quantity);
        return ResponseEntity.ok(new ApiResponse("Item quantity updated successfully", null));
    }

}
