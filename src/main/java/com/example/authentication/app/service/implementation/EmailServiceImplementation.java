package com.example.authentication.app.service.implementation;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.example.authentication.app.service.EmailService;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImplementation implements EmailService{

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void sendConfirmationEmail(String name, String to, String token) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Registration Confirmation");

            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("confirmationURL", "http://localhost:8080/api/auth/verify?token=" + token); // Replace with your

            String htmlContent = templateEngine.process("email/emailConfirmationTemplate", context);
            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            System.out.println("message : " + e.getMessage());
            System.out.println("cause : " + e.getCause());
        }
    }

}
