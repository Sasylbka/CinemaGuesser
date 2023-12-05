package com.example.demo.config;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final String senderEmail;

    public EmailService(JavaMailSender javaMailSender, @Value("${spring.mail.username}") String senderEmail) {
        this.javaMailSender = javaMailSender;
        this.senderEmail = senderEmail;
    }

    public void sendResetPasswordRequestToUser(String email, String username, Integer resetCode) {
        String resetPasswordEmailTemplate = "templates/reset-password.html";
        String subject = "Сброс пароля на CinemaGuesser";

        sendEmailWithTemplate(email, username, subject, resetCode, resetPasswordEmailTemplate);
    }

    public void sendEmailWithTemplate(String email, String username, String subject, Integer resetCode, String template) {

        String senderName = "CinemaGuesser";

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail, senderName);
            helper.setTo(email);
            helper.setSubject(subject);

            ClassPathResource resource = new ClassPathResource(template);
            String content = new String(Files.readAllBytes(resource.getFile().toPath()));

            content = content.replace("{{username}}", username);
            content = content.replace("{{resetCode}}", resetCode.toString());

            helper.setText(content, true);

            javaMailSender.send(message);

        } catch (MessagingException | IOException exception) {
            exception.printStackTrace();
        }
    }

}