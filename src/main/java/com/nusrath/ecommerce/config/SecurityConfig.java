package com.nusrath.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nusrath.ecommerce.service.AuthService;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final AuthService authService;
    
    public SecurityConfig(AuthService authService) {
        this.authService = authService;

    }

    @Bean 
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
        .userDetailsService(authService)
        .passwordEncoder(passwordEncoder())
        .and()
        .build();
    }

 @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     http
             .csrf(csrf -> csrf.disable())
             .authorizeHttpRequests(auth -> auth
                             .requestMatchers("/home", "users/register", "users/login" ).permitAll()
                             .anyRequest().authenticated()
             )
             .httpBasic(); 
    return http.build();
}

    
}
