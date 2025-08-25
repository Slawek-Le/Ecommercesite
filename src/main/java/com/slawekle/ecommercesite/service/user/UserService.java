package com.slawekle.ecommercesite.service.user;

import java.lang.foreign.Linker.Option;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.slawekle.ecommercesite.dtos.UserCartOrdersDto;
import com.slawekle.ecommercesite.dtos.UserDto;
import com.slawekle.ecommercesite.model.User;
import com.slawekle.ecommercesite.repository.UserRepository;
import com.slawekle.ecommercesite.request.CreateUserRequest;
import com.slawekle.ecommercesite.request.UpdateUserRequest;
import com.slawekle.ecommercesite.service.cart.ICartService;
import com.slawekle.ecommercesite.service.order.IOrderService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final IOrderService orderService;
    private final ICartService cartService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(CreateUserRequest userRequest) {
        return Optional.of(userRequest).filter(user -> !userRepository.existsByEmail(user.getEmail()))
                .map(req -> {
                    User newUser = new User();
                    newUser.setFirstName(req.getFirstName());
                    newUser.setLastName(req.getLastName());
                    newUser.setEmail(req.getEmail());
                    newUser.setPassword(passwordEncoder.encode(req.getPassword()));
                    return userRepository.save(newUser);
                })
                .orElseThrow(() -> new EntityExistsException(
                        "User with email " + userRequest.getEmail() + " already exists."));
    }

    @Override
    public User updateUser(UpdateUserRequest userRequest, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(userRequest.getFirstName());
            existingUser.setLastName(userRequest.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found."));
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found."));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(user -> userRepository.delete(user),
                        () -> {
                            throw new EntityNotFoundException("User with ID " + userId + " not found.");
                        });
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> convertToDtoList(List<User> users) {
        return users.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public UserCartOrdersDto convertToCartOrdersDto(Long userId) {
        UserCartOrdersDto dto = new UserCartOrdersDto();
        User user = getUserById(userId);
        dto.setUser(convertToDto(user));
        dto.setCart(cartService.convertToDto(cartService.getCartByUserId(userId)));
        dto.setOrders(orderService.convertToDtoList(orderService.getUserOrders(userId)));
        return dto;
    }

    @Override
    public User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new EntityNotFoundException("Log in required."));
    }

}
