package org.example.backend_vet_in_house.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendPasswordResetCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Código de recuperación - Vet in House");
        message.setText("Tu código de verificación es: " + code + "\nEste código expira en 3 minutos.");
        mailSender.send(message);
    }
}
