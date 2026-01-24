package com.github.pmbdev.global_finance_api.controller;

import com.github.pmbdev.global_finance_api.repository.entity.UserEntity;
import com.github.pmbdev.global_finance_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // To recieve web gets
@RequestMapping("/api/users") // Direction
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping // For POST queries
    public ResponseEntity<UserEntity> register(@RequestBody UserEntity user){
        UserEntity savedUser = userService.createUser(user);

        //201 code (CREATED) to reply
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PostMapping("/login") // login request
    public ResponseEntity<String> login(@RequestBody com.github.pmbdev.global_finance_api.controller.dto.LoginRequest loginRequest) {

        String token = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

        return ResponseEntity.ok(token);
    }
}
