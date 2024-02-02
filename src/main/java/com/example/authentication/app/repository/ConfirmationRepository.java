package com.example.authentication.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.authentication.app.entity.user.Confirmation;

public interface ConfirmationRepository extends JpaRepository<Confirmation , Long>{
    Confirmation findByToken(String token);
}
