package com.slawekle.ecommercesite.security.user;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.slawekle.ecommercesite.model.User;
import com.slawekle.ecommercesite.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShopUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = Optional
                .ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

        return ShopUserDetails.buildUserDetails(user);
    }

}
