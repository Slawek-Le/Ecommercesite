package com.slawekle.ecommercesite.dtos;

import java.util.List;


import lombok.Data;

@Data
public class UserCartOrdersDto {
    private UserDto user;
    private CartDto cart;
    private List<OrderDto> orders;
}
