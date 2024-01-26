package com.example.authentication.app.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authentication.app.JsonRsponse.JsonResponse;

import io.jsonwebtoken.JwtException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api")
public class ProductController {

    @GetMapping("/products")
    public ResponseEntity<JsonResponse> getProducts() throws JwtException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ADMIN"))) {
            return new ResponseEntity<JsonResponse>(
                    new JsonResponse(HttpStatus.OK.value(), "Hello, ADMIN! ."),
                    HttpStatus.OK);

        } else {
            return new ResponseEntity<JsonResponse>(
                    new JsonResponse(HttpStatus.FORBIDDEN.value(), "Ooops, you're not allowed to see this."),
                    HttpStatus.FORBIDDEN);

        }
    }
}
