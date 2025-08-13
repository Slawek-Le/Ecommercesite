package com.slawekle.ecommercesite.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.slawekle.ecommercesite.dtos.OrderDto;
import com.slawekle.ecommercesite.dtos.OrderItemDto;
import com.slawekle.ecommercesite.enums.OrderStatus;
import com.slawekle.ecommercesite.model.Cart;
import com.slawekle.ecommercesite.model.Order;
import com.slawekle.ecommercesite.model.OrderItem;
import com.slawekle.ecommercesite.model.Product;
import com.slawekle.ecommercesite.repository.OrderRepository;
import com.slawekle.ecommercesite.repository.ProductRepository;
import com.slawekle.ecommercesite.service.cart.ICartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ICartService cartService;

    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItems = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItems));
        order.setTotalAmount(calculateTotalAmount(orderItems));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getItems().stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    product.setInventory(product.getInventory() - cartItem.getQuantity());
                    productRepository.save(product);
                    return new OrderItem(order, product, cartItem.getQuantity(), cartItem.getUnitPrice());
                })
                .toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public OrderDto convertToDto(Order order) {
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        orderDto.setOrderItems(order.getOrderItems().stream()
                .map(item -> modelMapper.map(item, OrderItemDto.class))
                .collect(Collectors.toList()));
        return orderDto;
    }

    @Override
    public List<OrderDto> convertToDtoList(List<Order> orders) {
        return orders.stream()
                .map(this::convertToDto)
                .toList();
    }

   

}
