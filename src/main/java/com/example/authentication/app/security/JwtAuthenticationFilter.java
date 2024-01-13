package com.example.authentication.app.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.authentication.app.exception.CustomExceptionHandler;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");

        System.out.println("the Authorization header : "+authHeader);
        final String token;
        final String userEmail;


        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // No authentication in header
            CustomExceptionHandler.handleAuthenticationError(response, "JWT token is missing or not in the expected format");
            return;
        }

        token = authHeader.substring(7);
        System.out.println("token from the header : "+token);
        try {
            userEmail = jwtService.extractUsername(token);
            System.out.println("username form the header from the jwtfilter: " + userEmail);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                System.out.println("inside the first if : " + userDetails.getUsername());
                if (jwtService.isTokenValid(token, userDetails)) {
                    System.out.println("the token is valid ");
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }
            }
        } catch (MalformedJwtException ex) {
            CustomExceptionHandler.handleAuthenticationError(response, "Invalid JWT token");
            return;

        }



        filterChain.doFilter(request, response);
    }



}
