package com.github.pmbdev.global_finance_api.service;

import com.github.pmbdev.global_finance_api.repository.entity.UserEntity;

public interface UserService {
    UserEntity createUser(UserEntity user);

    String login(String email, String password);
}