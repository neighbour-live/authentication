package com.app.middleware.resources.services.implementation;

import com.app.middleware.persistence.dto.EmailNotificationDto;
import com.app.middleware.resources.services.EmailService;
import com.app.middleware.utility.Constants;
import com.app.middleware.utility.ObjectUtils;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.logging.Handler;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    public JavaMailSender javaMailSender;

    @Value("${email.from}")
    private String emailFrom;

    @Value("${email.api-key}")
    private String sendgridAPIKey;

    @Override
    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    @Override
    public void sendEmailFromExternalApi(EmailNotificationDto emailNotificationDto) throws IOException {



        Email from = new Email();
        from.setEmail(emailFrom);

        Email to = new Email();
        to.setEmail(emailNotificationDto.getTo());


        Mail mail = new Mail();
        mail.setFrom(from);

        Personalization personalization = new Personalization();
        personalization.addTo(to);
        personalization.setSubject(emailNotificationDto.getSubject());

        if(!ObjectUtils.isNull(emailNotificationDto.getPlaceHolders())){
            for(String placeHolder : emailNotificationDto.getPlaceHolders().keySet()){
                personalization.addDynamicTemplateData(placeHolder,emailNotificationDto.getPlaceHolders().get(placeHolder));
            }
        }
        mail.addPersonalization(personalization);

        Content content = new Content();
        content.setType("text/html");
        content.setValue(" "); // <- must be a string with at least one character
        mail.addContent(content);
        mail.setTemplateId(emailNotificationDto.getTemplate());

        SendGrid sg = new SendGrid(sendgridAPIKey);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw new IOException(ex);
        }
    }
}
