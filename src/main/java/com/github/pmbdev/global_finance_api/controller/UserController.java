package com.github.pmbdev.global_finance_api.controller;

import com.github.pmbdev.global_finance_api.controller.dto.AuthResponse;
import com.github.pmbdev.global_finance_api.controller.dto.RegisterRequest;
import com.github.pmbdev.global_finance_api.controller.dto.UserResponse;
import com.github.pmbdev.global_finance_api.repository.entity.UserEntity;
import com.github.pmbdev.global_finance_api.service.UserService;
import com.github.pmbdev.global_finance_api.controller.dto.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController // To recieve web gets
@RequestMapping("/api/users") // Direction
@RequiredArgsConstructor
@Tag(name = "Users", description = "User registration, authentication, and session management")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Register user",
            description = "Register a new user in the database.")
    @ApiResponse(responseCode = "201", description = "Create successful")
    @ApiResponse(responseCode = "400", description = "Invalid credentials")
    @ApiResponse(responseCode = "409", description = "Email already exists")
    @PostMapping("/register") // To POST queries
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request){
        UserResponse savedUser = userService.createUser(request);

        //201 code (CREATED) to reply
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @Operation(
            summary = "Login",
            description = "Login into a user account.")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "400", description = "Invalid credentials")
    @PostMapping("/login") // To login request
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        AuthResponse token = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

        return ResponseEntity.ok(token);
    }

    @Operation(
            summary = "Token Demo",
            description = "A simple endpoint to verify if your JWT token is valid.")
    @ApiResponse(responseCode = "200", description = "Token is valid")
    @GetMapping("/demo")
    public ResponseEntity<String> demo() {
        return ResponseEntity.ok("Hi! If you read this it's because you have a valid token.");
    }
}
