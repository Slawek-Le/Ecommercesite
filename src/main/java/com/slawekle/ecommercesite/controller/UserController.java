package com.slawekle.ecommercesite.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slawekle.ecommercesite.dtos.UserCartOrdersDto;
import com.slawekle.ecommercesite.dtos.UserDto;
import com.slawekle.ecommercesite.model.User;
import com.slawekle.ecommercesite.request.CreateUserRequest;
import com.slawekle.ecommercesite.request.UpdateUserRequest;
import com.slawekle.ecommercesite.response.ApiResponse;
import com.slawekle.ecommercesite.service.user.IUserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;

    @GetMapping("/user/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        UserDto userDto = userService.convertToDto(user);
        return ResponseEntity.ok(new ApiResponse("User retrieved successfully", userDto));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest userRequest) {
        User createdUser = userService.createUser(userRequest);
        UserDto createdUserDto = userService.convertToDto(createdUser);
        return ResponseEntity.ok(new ApiResponse("User added successfully", createdUserDto));
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long userId,
            @RequestBody UpdateUserRequest userRequest) {
        User updatedUser = userService.updateUser(userRequest, userId);
        UserDto updatedUserDto = userService.convertToDto(updatedUser);
        return ResponseEntity.ok(new ApiResponse("User updated successfully", updatedUserDto));
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse("User deleted successfully", null));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDto> userDtos = userService.convertToDtoList(users);
        return ResponseEntity.ok(new ApiResponse("All users retrieved successfully", userDtos));
    }

    @GetMapping("/user/{userId}/userdata")
    public ResponseEntity<ApiResponse> getUserData(@PathVariable Long userId) {
        UserCartOrdersDto cartOrdersDto = userService.convertToCartOrdersDto(userId);
        return ResponseEntity.ok(new ApiResponse("User data retrieved successfully", cartOrdersDto));
    }
    

}
