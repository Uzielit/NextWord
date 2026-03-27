package com.nextword.backend.shared.email;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

    public void sendAccountVerificationEmail(String toEmail, String verificationCode) {
        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Verifica tu cuenta - Next Word");

            String htmlContent = "<!DOCTYPE html>" +
                    "<html lang='es'>" +
                    "<head>" +
                    "<meta charset='UTF-8'>" +
                    "<style>" +
                    "  body { font-family: Arial, sans-serif; background-color: #f4f4f5; margin: 0; padding: 40px 0; }" +
                    "  .container { max-width: 500px; margin: 0 auto; background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }" +
                    "  .header { background-color: #8A2BE2; padding: 30px; text-align: center; }" + // <-- El moradito de Next Word
                    "  .header h1 { color: #ffffff; margin: 0; font-size: 28px; letter-spacing: 1px; }" +
                    "  .content { padding: 40px 30px; text-align: center; color: #333333; }" +
                    "  .content p { font-size: 16px; line-height: 1.5; color: #4b5563; }" +
                    "  .code-box { background-color: #f3e8ff; border: 2px dashed #8A2BE2; border-radius: 12px; padding: 20px; margin: 30px 0; font-size: 36px; font-weight: bold; color: #8A2BE2; letter-spacing: 8px; }" +
                    "  .footer { background-color: #f9fafb; padding: 20px; text-align: center; font-size: 12px; color: #9ca3af; border-top: 1px solid #e5e7eb; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "  <div class='container'>" +
                    "    <div class='header'>" +
                    "      <h1>Next Word</h1>" +
                    "    </div>" +
                    "    <div class='content'>" +
                    "      <h2 style='margin-top: 0;'>¡Bienvenido a la plataforma!</h2>" +
                    "      <p>Para completar tu registro y asegurar tu cuenta, por favor ingresa el siguiente código de verificación en la aplicación:</p>" +
                    "      <div class='code-box'>" + verificationCode + "</div>" +
                    "      <p style='font-size: 14px; color: #6b7280;'>Este código expirará en <strong>15 minutos</strong>.</p>" +
                    "      <p style='font-size: 14px; color: #6b7280;'>Si tú no creaste esta cuenta, puedes ignorar este correo de forma segura.</p>" +
                    "    </div>" +
                    "    <div class='footer'>" +
                    "      © 2026 Next Word. Todos los derechos reservados.<br>" +
                    "      Jiutepec, Morelos, México." +
                    "    </div>" +
                    "  </div>" +
                    "</body>" +
                    "</html>";


            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (Exception e) {
            System.out.println("Error enviando correo HTML: " + e.getMessage());
        }
    }
}
