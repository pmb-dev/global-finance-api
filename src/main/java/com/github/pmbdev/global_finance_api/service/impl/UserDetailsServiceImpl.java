package com.github.pmbdev.global_finance_api.service.impl;

import com.github.pmbdev.global_finance_api.exception.custom.UserNotFoundException;
import com.github.pmbdev.global_finance_api.repository.UserRepository;
import com.github.pmbdev.global_finance_api.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email){
        //Searching for the user in the DB
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found."));

        // Convert to User object understood by Spring Security
        return new User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                new ArrayList<>() // Roles
        );

    }
}
