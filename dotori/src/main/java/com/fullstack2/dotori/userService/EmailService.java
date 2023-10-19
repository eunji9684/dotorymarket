package com.fullstack2.dotori.userService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.fullstack2.dotori.PasswordGenerator;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendTemporaryPassword(String toEmail) throws MessagingException {
        // 임시 비밀번호 생성
        String temporaryPassword = PasswordGenerator.generateRandomPassword(10);

        // 이메일 전송
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("임시 비밀번호 발급");
        helper.setText("임시 비밀번호: " + temporaryPassword);

        emailSender.send(message);
    }
}
