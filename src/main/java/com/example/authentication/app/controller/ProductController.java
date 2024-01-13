package com.example.authentication.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.JwtException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api")
public class ProductController {
    
    @GetMapping("/products")
    public ResponseEntity<String> getProducts() throws JwtException{
        return ResponseEntity.ok("hello im a secured endpoint");
    }
    
}
