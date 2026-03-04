package com.nusrath.ecommerce.service;

import com.nusrath.ecommerce.dto.LoginRequest;
import com.nusrath.ecommerce.dto.SignUpRequest;
import com.nusrath.ecommerce.dto.UserDetailResponse;
import com.nusrath.ecommerce.model.Role;
import com.nusrath.ecommerce.model.User;
import com.nusrath.ecommerce.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nusrath.ecommerce.util.AuthUtil;
import com.nusrath.ecommerce.util.JwtUtil;
import com.nusrath.ecommerce.dto.LoginResponse;


@Service
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtil securityUtil;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AccountService(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthUtil securityUtil,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.securityUtil = securityUtil;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public UserDetailResponse register(SignUpRequest request) {

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with this email"));

        return new LoginResponse(token, mapToResponse(user));
    }


    public List<UserDetailResponse> getAllUsers() {

        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
     public UserDetailResponse getUserDetails(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String loggedInEmail = securityUtil.getCurrentUserEmail();
        boolean isAdmin = securityUtil.isAdmin();

        if (!loggedInEmail.equals(user.getEmail()) && !isAdmin) {
            throw new AccessDeniedException("You can only access your own profile");
        }

        return mapToResponse(user);
    }



    private UserDetailResponse mapToResponse(User user) {
        UserDetailResponse res = new UserDetailResponse();
        res.setId(user.getId());
        res.setFirstName(user.getFirstName());
        res.setLastName(user.getLastName());
        res.setEmail(user.getEmail());
        res.setRole(user.getRole());
        return res;
    }
}