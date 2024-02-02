package com.example.authentication.app.service;

public interface EmailService {
    void sendConfirmationEmail(String name,String to,String token);
}
