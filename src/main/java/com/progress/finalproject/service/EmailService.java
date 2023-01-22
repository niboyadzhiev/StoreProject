package com.progress.finalproject.service;

import com.progress.finalproject.model.user.User;

public interface EmailService {
    static final String PASSWORD_TEMPLATE = "Dear %s %s,\nThank you for your registration.\nYour password is: %s";
    void sendEmail(User receiver, String password);
}
