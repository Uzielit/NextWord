package com.nextword.backend.shared.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Método principal para mandar el código de recuperación
    public void sendPasswordResetEmail(String toEmail, String resetCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);

        message.setSubject("Recuperación de Contraseña - NextWord");

        message.setText("Hola,\n\n" +
                "Recibimos una solicitud para restablecer tu contraseña.\n" +
                "Tu código de seguridad temporal es: " + resetCode +
                "\n\n" +
                "Tiene duracion de 15 minutos \n " +
                "Si no fuiste tú, ignora este correo.\n\n" +
                "Atentamente,\nEl equipo de NextWord");

        mailSender.send(message);
    }
}
