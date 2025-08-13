package com.slawekle.ecommercesite.service.cart;

import java.math.BigDecimal;

import com.slawekle.ecommercesite.dtos.CartDto;
import com.slawekle.ecommercesite.dtos.CartItemDto;
import com.slawekle.ecommercesite.model.Cart;
import com.slawekle.ecommercesite.model.CartItem;
import com.slawekle.ecommercesite.model.User;

public interface ICartService {
    Cart getCartById(Long cartId);
    Cart getCartByUserId(Long userId);
    void clearCart(Long cartId);
    Cart initializeCart(User user);
    BigDecimal getTotalPrice(Long cartId);

    CartDto convertToDto(Cart cart);

    CartItemDto convertCartItemToDto(CartItem cartItem);


}
