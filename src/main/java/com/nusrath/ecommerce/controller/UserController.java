package com.nusrath.ecommerce.controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.nusrath.ecommerce.dto.ApiResponse;
import com.nusrath.ecommerce.dto.LoginRequest;
import com.nusrath.ecommerce.dto.LoginResponse;
import com.nusrath.ecommerce.dto.SignUpRequest;
import com.nusrath.ecommerce.dto.UserDetailResponse;
import com.nusrath.ecommerce.service.AccountService;

@RestController
@RequestMapping("/users")
public class UserController {
    
    private final AccountService AccountService;
    
    public UserController(AccountService AccountService) {
        this.AccountService = AccountService;
    }

     @PostMapping("/register")
    public ApiResponse<UserDetailResponse> register(@RequestBody SignUpRequest request) {
        UserDetailResponse user = AccountService.register(request);
        return ApiResponse.success(201, "User registered successfully", user);
    }
    

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse login = AccountService.login(request);
        return ApiResponse.success(200, "Login successful", login);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<java.util.List<UserDetailResponse>> getAllUsers() {
        java.util.List<UserDetailResponse> users = AccountService.getAllUsers();
        return ApiResponse.success(200, "Users fetched successfully", users);
    }
    

    @GetMapping("/{id}")
    public ApiResponse<UserDetailResponse> getUser(@PathVariable Long id) {
        UserDetailResponse user = AccountService.getUserDetails(id);
        return ApiResponse.success(200, "User details fetched successfully", user);
    }
}
