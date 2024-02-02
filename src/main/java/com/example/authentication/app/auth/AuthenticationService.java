package com.example.authentication.app.auth;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

import org.hibernate.PropertyValueException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.authentication.app.entity.user.Confirmation;
import com.example.authentication.app.entity.user.Role;
import com.example.authentication.app.entity.user.User;
import com.example.authentication.app.repository.ConfirmationRepository;
import com.example.authentication.app.repository.UserRepository;
import com.example.authentication.app.security.JwtService;
import com.example.authentication.app.service.EmailService;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationRepository confirmationRepository;
    private final EmailService emailService;

    
    public AuthenticationResponse register(RegisterRequest request) throws SQLIntegrityConstraintViolationException ,
            PropertyValueException {
        User user = User.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .roles(Role.USER.toString())
            .enabled(true)
            .build();
        
        userRepository.save(user);

        // using this part when i want that confirmation stuff
        // Confirmation confirmation = new Confirmation(user);
        // confirmationRepository.save(confirmation);
        // emailService.sendConfirmationEmail(user.getFirstName(), user.getEmail(), confirmation.getToken());

        
        String jwtToken = jwtService.generateToken(user);


        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException e) {
            throw new JwtException("Authentication failed: " + e.getMessage());
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new JwtException("User not found after authentication"));

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public  Boolean validateToken(String token)  {
        Confirmation confirmation = confirmationRepository.findByToken(token);
        Optional<User> user = userRepository.findByEmail(confirmation.getUser().getEmail());

        if (user.isPresent()) {
            User realUser = user.get();
            realUser.setEnabled(true);
            userRepository.save(realUser);
            confirmationRepository.delete(confirmation);
            return true;
        }
        return false;
    }
    
}
