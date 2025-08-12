package com.slawekle.ecommercesite.service.user;

import java.util.List;

import com.slawekle.ecommercesite.model.User;
import com.slawekle.ecommercesite.request.CreateUserRequest;
import com.slawekle.ecommercesite.request.UpdateUserRequest;

public interface IUserService {
    User createUser(CreateUserRequest userRequest);

    User updateUser(UpdateUserRequest userRequest, Long userId);

    User getUserById(Long userId);

    void deleteUser(Long userId);

    List<User> getAllUsers();

}
