package com.slawekle.ecommercesite.dtos;

import lombok.Data;

@Data
public class UserDto {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
}
