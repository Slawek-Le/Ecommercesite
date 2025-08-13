package com.slawekle.ecommercesite.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import com.slawekle.ecommercesite.enums.OrderStatus;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private List<OrderItemDto> orderItems = new ArrayList<>();
}
