package com.slawekle.ecommercesite.service.order;

import java.util.List;

import com.slawekle.ecommercesite.dtos.OrderDto;
import com.slawekle.ecommercesite.model.Order;




public interface IOrderService {
    Order placeOrder(Long userId);
    List<Order> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
    List<OrderDto> convertToDtoList(List<Order> orders);

}
