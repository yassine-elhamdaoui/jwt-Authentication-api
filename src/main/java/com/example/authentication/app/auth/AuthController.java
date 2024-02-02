package com.example.authentication.app.auth;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.authentication.app.JsonRsponse.JsonResponse;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) throws SQLIntegrityConstraintViolationException{
        // using this part when i want that confirmation stuff
        // return new ResponseEntity<JsonResponse>(new JsonResponse(201,
        //         "Account registered successfully , please verify the account by clicking on the link sent to your email address."),
        //         HttpStatus.CREATED);

        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request)throws JwtException{
        System.out.println(request);
        return ResponseEntity.ok(authenticationService.authenticate(request));

    }

    @GetMapping("/verify")
    public ModelAndView confirmUserAccount(@RequestParam("token") String token){
        Boolean isUserConfirmed = authenticationService.validateToken(token);
        ModelAndView modelAndView = new ModelAndView();

        // return new ResponseEntity<JsonResponse>(new JsonResponse(200, "you account has been confirmed"), HttpStatus.OK);
        modelAndView.setViewName("verification-success"); // Thymeleaf template name (without .html extension)
        modelAndView.setStatus(HttpStatus.OK);
        
        return modelAndView;

    }
}
