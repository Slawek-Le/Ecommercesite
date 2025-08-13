package com.slawekle.ecommercesite.service.cart;


import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.slawekle.ecommercesite.dtos.CartDto;
import com.slawekle.ecommercesite.dtos.CartItemDto;
import com.slawekle.ecommercesite.model.Cart;
import com.slawekle.ecommercesite.model.CartItem;
import com.slawekle.ecommercesite.model.User;
import com.slawekle.ecommercesite.repository.CartItemRepository;
import com.slawekle.ecommercesite.repository.CartRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;

    @Override
    public Cart getCartById(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with id: " + cartId));
        
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public void clearCart(Long cartId) {
        Cart cart = getCartById(cartId);
        cartItemRepository.deleteAllByCartId(cartId);
        cart.clearCart();
        cartRepository.deleteById(cartId);

    }

    @Override
    public Cart initializeCart(User user) {
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    @Override
    public BigDecimal getTotalPrice(Long cartId) {
        Cart cart = getCartById(cartId);
        return cart.getTotalAmount();
    }

    @Override
    public CartDto convertToDto(Cart cart) {
        CartDto cartDto = modelMapper.map(cart, CartDto.class);
        
        cartDto.setItems(cart.getItems().stream()
                .map(cartItem -> convertCartItemToDto(cartItem))
                .collect(Collectors.toList()));
        return cartDto;
    }

    @Override
    public CartItemDto convertCartItemToDto(CartItem cartItem) {
        return modelMapper.map(cartItem, CartItemDto.class);    
    }


}
