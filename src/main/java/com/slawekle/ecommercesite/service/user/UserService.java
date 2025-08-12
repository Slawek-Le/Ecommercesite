package com.slawekle.ecommercesite.service.user;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.slawekle.ecommercesite.model.User;
import com.slawekle.ecommercesite.repository.UserRepository;
import com.slawekle.ecommercesite.request.CreateUserRequest;
import com.slawekle.ecommercesite.request.UpdateUserRequest;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;

    @Override
    public User createUser(CreateUserRequest userRequest) {
        return Optional.of(userRequest).filter(user -> !userRepository.existsByEmail(user.getEmail()))
                .map(req -> {
                    User newUser = new User();
                    newUser.setFirstName(req.getFirstName());
                    newUser.setLastName(req.getLastName());
                    newUser.setEmail(req.getEmail());
                    newUser.setPassword(req.getPassword());
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

}
