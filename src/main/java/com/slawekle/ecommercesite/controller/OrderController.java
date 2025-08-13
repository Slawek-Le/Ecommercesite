package com.slawekle.ecommercesite.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.slawekle.ecommercesite.dtos.OrderDto;
import com.slawekle.ecommercesite.model.Order;
import com.slawekle.ecommercesite.response.ApiResponse;
import com.slawekle.ecommercesite.service.order.IOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("/user/order")
    public ResponseEntity<ApiResponse> placeOrder(@RequestParam Long userId) {
        Order order = orderService.placeOrder(userId);
        OrderDto orderDto = orderService.convertToDto(order);
        return ResponseEntity.ok(new ApiResponse("Order placed successfully", orderDto));
    }

    @GetMapping("/user/{userId}/orders")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
        List<Order> orders = orderService.getUserOrders(userId);
        List<OrderDto> orderDtos = orderService.convertToDtoList(orders);
        return ResponseEntity.ok(new ApiResponse("User orders retrieved successfully", orderDtos));
    }

}
