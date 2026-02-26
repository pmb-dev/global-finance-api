package com.github.pmbdev.global_finance_api.service;

import com.github.pmbdev.global_finance_api.controller.dto.AuthResponse;
import com.github.pmbdev.global_finance_api.controller.dto.RegisterRequest;
import com.github.pmbdev.global_finance_api.controller.dto.UserResponse;
import com.github.pmbdev.global_finance_api.repository.entity.UserEntity;

public interface UserService {
    UserResponse createUser(RegisterRequest user);

    AuthResponse login(String email, String password);
}