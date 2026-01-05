package com.nusrath.ecommerce.controller;
import org.springframework.web.bind.annotation.*;

import com.nusrath.ecommerce.dto.ApiResponse;
import com.nusrath.ecommerce.dto.LoginRequest;
import com.nusrath.ecommerce.dto.SignUpRequest;
import com.nusrath.ecommerce.dto.UserDetailResponse;
import com.nusrath.ecommerce.service.AccountService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final AccountService AccountService;
    
    public UserController(AccountService AccountService) {
        this.AccountService = AccountService;
    }

    // Signup
    @PostMapping("/register")
    public ApiResponse<UserDetailResponse> registerUser(@RequestBody SignUpRequest request) {
        return AccountService.register(request);
    }

    // Login
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        boolean success = AccountService.login(request);
        if (success) {
            return "Login successful";
        } else {
            return "Login failed";
        }
}

    //getUserProfile
    @GetMapping("/{id}")
    public ApiResponse<UserDetailResponse> getUser(@PathVariable Long id){

        return AccountService.getUserDetails(id);
    }

}
