package com.progress.finalproject.service.impl;

import com.progress.finalproject.model.user.User;
import com.progress.finalproject.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;



    @Override
    public void sendEmail(User receiver, String password) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(receiver.getEmail());
        mailMessage.setText(String.format(PASSWORD_TEMPLATE, receiver.getFirstName(), receiver.getLastName(),password));
        mailMessage.setSubject("Registration");
        javaMailSender.send(mailMessage);

    }

    @Override
    public void sendNewPassword(User receiver, String password) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(receiver.getEmail());
        mailMessage.setText(String.format(NEW_PASSWORD_TEMPLATE, receiver.getFirstName(), receiver.getLastName(),password));
        mailMessage.setSubject("New password");
        javaMailSender.send(mailMessage);
    }


}
