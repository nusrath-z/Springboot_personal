package com.nusrath.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.nusrath.ecommerce.service.AuthService;
import com.nusrath.ecommerce.util.JwtRequestFilter;


@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final AuthService authService;
    private final com.nusrath.ecommerce.util.JwtUtil jwtUtil;
    
    public SecurityConfig(AuthService authService, com.nusrath.ecommerce.util.JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;

    }

    

    @Bean 
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(authService, jwtUtil);}
 
        @Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
}

 @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     http
             .csrf(csrf -> csrf.disable())
             .sessionManagement(sm -> sm.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS))
             .authorizeHttpRequests(auth -> auth
                             .requestMatchers("/home", "/users/register", "/users/login").permitAll()
                             .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                             .anyRequest().authenticated()
             );
    http.addFilterBefore(jwtRequestFilter(), org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
    return http.build();
}

    
}
