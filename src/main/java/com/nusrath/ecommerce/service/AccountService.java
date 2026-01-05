package com.nusrath.ecommerce.service;

import com.nusrath.ecommerce.dto.ApiResponse;
import com.nusrath.ecommerce.dto.LoginRequest;
import com.nusrath.ecommerce.dto.SignUpRequest;
import com.nusrath.ecommerce.dto.UserDetailResponse;
import com.nusrath.ecommerce.model.Role;
import com.nusrath.ecommerce.model.User;
import com.nusrath.ecommerce.repository.UserRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
   
    
    //Signup
   public ApiResponse<UserDetailResponse> register(SignUpRequest request) {

    // 1. Create entity
    User user = new User();
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    // Security rule: role decided by backend
    user.setRole(Role.USER);

    // 2. Save
    User savedUser = userRepository.save(user);

    // 3. Map to response DTO
    UserDetailResponse userResponse = mapToResponse(savedUser);

    // 4. Wrap in ApiResponse
    ApiResponse<UserDetailResponse> response = new ApiResponse<>();
    response.setStatusCode(201);
    response.setMessage("User registered successfully");
    response.setData(userResponse);

    return response;
}

    
    //Login
      public boolean login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found with this email"));
        
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw  new RuntimeException("Invalid password");
        }

        return true;
    }

    //Get User Details
    public ApiResponse<UserDetailResponse> getUserDetails(Long id){
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String loggedInUserEmail = authentication.getName();
    boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

    if (!loggedInUserEmail.equals(user.getEmail()) && !isAdmin) {
        throw new AccessDeniedException("You can only access your own profile");
    }

    UserDetailResponse userDetails = mapToResponse(user);

    ApiResponse<UserDetailResponse> response = new ApiResponse<>();
    response.setStatusCode(200);
    response.setMessage("User details fetched successfully");
    response.setData(userDetails);

    return response;
}

   
    private UserDetailResponse mapToResponse(User user) {
        UserDetailResponse response = new UserDetailResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }
}
