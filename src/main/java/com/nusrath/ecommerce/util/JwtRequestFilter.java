package com.nusrath.ecommerce.util;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.nusrath.ecommerce.service.AuthService;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain filterChain)
        throws ServletException, IOException {


    String authHeader = request.getHeader("Authorization");

    String email = null;
    String token= null;


  if (authHeader != null && authHeader.startsWith("Bearer ")) {
             token = authHeader.substring(7);
             email = jwtUtil.extractEmail(token);

        }
  

    try {
        
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = authService.loadUserByUsername(email);
            if (jwtUtil.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);

    } catch (io.jsonwebtoken.ExpiredJwtException e) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\":\"Token expired\"}");
    } catch (io.jsonwebtoken.JwtException e) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\":\"Invalid JWT token\"}");
    }
}

    // @Override
    // protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    //         throws ServletException, IOException {

    //     final String authorizationHeader = request.getHeader("Authorization");

    //     String username = null;
    //     String jwt = null;

    //     if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
    //         jwt = authorizationHeader.substring(7);
    //         username = jwtUtil.extractEmail(jwt);
    //     }

    //     if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
    //         UserDetails userDetails = authService.loadUserByUsername(username);
    //         if (jwtUtil.isTokenValid(jwt, userDetails)) {
    //             UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
    //                     userDetails, null, userDetails.getAuthorities());
    //             authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    //             SecurityContextHolder.getContext().setAuthentication(authToken);
    //         }
    //     }
    //     chain.doFilter(request, response);
    // }
}