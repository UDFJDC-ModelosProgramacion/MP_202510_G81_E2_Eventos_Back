package co.edu.udistrital.mdp.eventos.services.emailcontrol;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class EmailRegisterService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRegisterEmail(String toEmail, String name, String password, LocalDate registrationDate) {
        String subject = "¡Registro exitoso en EntradaYa!";
        String formattedDate = registrationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String body = "Hola " + name + ",\n\n"
                + "Te damos la bienvenida a EntradaYa.\n\n"
                + "Detalles de tu registro:\n"
                + "Correo: " + toEmail + "\n"
                + "Contraseña: " + password + "\n"
                + "Fecha de registro: " + formattedDate + "\n\n"
                + "¡Gracias por unirte a nuestra plataforma!\n\n"
                + "Equipo EntradaYa";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("hideseekmarketud@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}

