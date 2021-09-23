package com.bot.middleware.resources.services;

import com.bot.middleware.persistence.dto.EmailNotificationDto;
import org.springframework.mail.SimpleMailMessage;

import java.io.IOException;

public interface EmailService {

    void sendEmail(SimpleMailMessage message);

    void sendEmailFromExternalApi(EmailNotificationDto emailNotificationDto) throws IOException;
}
