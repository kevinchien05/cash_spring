package com.example.cash.event;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.example.cash.domain.User;
import com.example.cash.service.EmailService;
import com.example.cash.service.VerificationTokenService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private VerificationTokenService tokenService;

    @Autowired
    private EmailService emailService;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        tokenService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Email Verification";
        String confirmationUrl = "http://localhost:5173/verify-email?token=" + token;
        String message = "Click the link to verify your email: " + confirmationUrl;

        emailService.sendEmail(recipientAddress, subject, message);
    }
}
