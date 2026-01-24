package com.github.pmbdev.global_finance_api.service.impl;

import com.github.pmbdev.global_finance_api.repository.UserRepository;
import com.github.pmbdev.global_finance_api.repository.entity.UserEntity;
import com.github.pmbdev.global_finance_api.security.JwtUtils;
import com.github.pmbdev.global_finance_api.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // It is like the constructor for UserRepository (lombok)
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public UserEntity createUser(UserEntity user){
        user.setPassword(passwordEncoder.encode(user.getPassword())); //To encode the password before saving it
        return userRepository.save(user);
    }

    @Override
    public String login(String email, String password) {
        // Search user with email
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Username not found."));

        // Verify password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Incorrect Password.");
        }

        // Generate Token
        return jwtUtils.generateToken(email);
    }
}
