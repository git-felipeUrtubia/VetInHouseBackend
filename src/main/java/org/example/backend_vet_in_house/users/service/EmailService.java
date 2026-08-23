package org.example.backend_vet_in_house.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String email;
    private final JavaMailSender mailSender;

    public void sendPasswordResetCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Código de recuperación - Vet in House");
        message.setText("Tu código de verificación es: " + code + "\nEste código expira en 3 minutos.");
        mailSender.send(message);
    }

    public void sendContactEmail(String nombre, String correo, String mascota, String mensaje) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        // Aquí pones el correo donde tú (el administrador) quieres recibir los mensajes
        mailMessage.setTo(email);

        mailMessage.setSubject("Nuevo mensaje de contacto: " + nombre);
        mailMessage.setText(
                "Has recibido un nuevo mensaje desde el formulario web:\n\n" +
                        "Nombre: " + nombre + "\n" +
                        "Correo de contacto: " + correo + "\n" +
                        "Mascota: " + (mascota != null && !mascota.isEmpty() ? mascota : "No especificada") + "\n\n" +
                        "Mensaje:\n" + mensaje
        );
        mailSender.send(mailMessage);
    }

    public void sendEmailUpdateCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to); // Se envía al NUEVO correo
        message.setSubject("Código de verificación - Cambio de correo");
        message.setText("Tu código para confirmar el cambio de correo es: " + code +
                "\nEste código expira en 15 minutos. Si no solicitaste esto, ignora este mensaje.");
        mailSender.send(message);
    }
}
