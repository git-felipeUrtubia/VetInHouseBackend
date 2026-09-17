package org.example.backend_vet_in_house.users.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${EMAIL}")
    private String adminEmail;

    @Value("${resend.api.key}")
    private String resendApiKey;

    private final String FROM_EMAIL = "Vet In House <contacto@lucci.cl>";

    public void sendPasswordResetCode(String to, String code) {
        Resend resend = new Resend(resendApiKey);
        try {
            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from(FROM_EMAIL)
                    .to(to)
                    .subject("Código de recuperación - Vet in House")
                    .html("<p>Tu código de verificación es: <strong>" + code + "</strong></p><p>Este código expira en 3 minutos.</p>")
                    .build();
            resend.emails().send(params);
        } catch (ResendException e) {
            throw new RuntimeException("Error al enviar el correo de recuperación", e);
        }
    }

    public void sendContactEmail(String nombre, String correo, String mascota, String mensaje) {
        Resend resend = new Resend(resendApiKey);
        try {
            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from(FROM_EMAIL)
                    .to(adminEmail)
                    .replyTo(correo) // Las respuestas a este correo le llegarán al cliente
                    .subject("Nuevo mensaje de contacto: " + nombre)
                    .html("<p>Has recibido un nuevo mensaje desde el formulario web:</p>" +
                            "<ul>" +
                            "<li><strong>Nombre:</strong> " + nombre + "</li>" +
                            "<li><strong>Correo:</strong> " + correo + "</li>" +
                            "<li><strong>Mascota:</strong> " + (mascota != null && !mascota.isEmpty() ? mascota : "No especificada") + "</li>" +
                            "</ul>" +
                            "<p><strong>Mensaje:</strong><br/>" + mensaje + "</p>")
                    .build();
            resend.emails().send(params);
        } catch (ResendException e) {
            throw new RuntimeException("Error al enviar el correo de contacto", e);
        }
    }

    public void sendEmailUpdateCode(String to, String code) {
        Resend resend = new Resend(resendApiKey);
        try {
            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from(FROM_EMAIL)
                    .to(to)
                    .subject("Código de verificación - Cambio de correo")
                    .html("<p>Tu código para confirmar el cambio de correo es: <strong>" + code + "</strong></p><p>Este código expira en 15 minutos. Si no solicitaste esto, ignora este mensaje.</p>")
                    .build();
            resend.emails().send(params);
        } catch (ResendException e) {
            throw new RuntimeException("Error al enviar el correo de actualización", e);
        }
    }

    public void sendEmailUpdateWarning(String oldEmail) {
        Resend resend = new Resend(resendApiKey);
        try {
            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from(FROM_EMAIL)
                    .to(oldEmail)
                    .subject("Alerta de seguridad - Cambio de correo solicitado")
                    .html("<p>Se ha solicitado un cambio de correo electrónico para tu cuenta.</p><p><strong>Si no fuiste tú, contacta a soporte inmediatamente y cambia tu contraseña.</strong></p>")
                    .build();
            resend.emails().send(params);
        } catch (ResendException e) {
            throw new RuntimeException("Error al enviar el correo de advertencia", e);
        }
    }
}