package com.app.middleware.resources.services;

import com.app.middleware.persistence.dto.EmailNotificationDto;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EmailService {

    void sendEmail(SimpleMailMessage message);

    void sendEmailFromExternalApi(EmailNotificationDto emailNotificationDto) throws IOException;

    void sendEmailFromAWSEmailSES(EmailNotificationDto emailNotificationDto) throws IOException, MessagingException;
}
