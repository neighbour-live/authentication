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

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.IOException;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    public JavaMailSender javaMailSender;

    @Value("${email.api-key}")
    private String sendGridApiKey;

    // Replace sender@example.com with your "From" address.
    // This address must be verified.
    static final String FROM = "social@neighbour.live";
    static final String FROMNAME = "Neighbour Connect";

    // Replace recipient@example.com with a "To" address. If your account
    // is still in the sandbox, this address must be verified.
    static final String TO = "abdulsami.se@gmail.com";

    // Replace smtp_username with your Amazon SES SMTP user name.
    static final String SMTP_USERNAME = "AKIAW53Q7GVWWA3RVQMU";

    // Replace smtp_password with your Amazon SES SMTP password.
    static final String SMTP_PASSWORD = "BJocUeGX1EkJl7yb72iWlHMiJAc5BQYaYz+g3tm11+Ql";

    // The name of the Configuration Set to use for this message.
    // If you comment out or remove this variable, you will also need to
    // comment out or remove the header below.
    static final String CONFIGSET = "no-reply-email";

    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
    // See https://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html#region-endpoints
    // for more information.
    static final String HOST = "email-smtp.eu-west-1.amazonaws.com";

    // The port you will connect to on the Amazon SES SMTP endpoint.
    static final int PORT = 587;

    static final String SUBJECT = "Amazon SES test (SMTP interface accessed using Java)";

    static final String BODY = String.join(
            System.getProperty("line.separator"),
            "<h1>Amazon SES SMTP Email Test</h1>",
            "<p>This email was sent with Amazon SES using the ",
            "<a href='https://github.com/javaee/javamail'>Javamail Package</a>",
            " for <a href='https://www.java.com'>Java</a>."
    );

    @Override
    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    @Override
    public void sendEmailFromExternalApi(EmailNotificationDto emailNotificationDto) throws IOException {

        Mail mail = new Mail();
        Email from = new Email();

        from.setEmail(Constants.SEND_GRID_FROM_EMAIL);
        mail.setFrom(from);

        Personalization personalization = new Personalization();

        Email to = new Email();
        to.setEmail(emailNotificationDto.getTo());
        personalization.addTo(to);

        if(!ObjectUtils.isNull(emailNotificationDto.getPlaceHolders())){
            for(String placeHolder : emailNotificationDto.getPlaceHolders().keySet()){
                personalization.addDynamicTemplateData(placeHolder,emailNotificationDto.getPlaceHolders().get(placeHolder));
            }
        }
        mail.addPersonalization(personalization);

        Content content = new Content();
        content.setType("text/html");
        content.setValue("Something");
        mail.addContent(content);
        mail.setTemplateId(emailNotificationDto.getTemplate());

        SendGrid sg = new SendGrid(sendGridApiKey);

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
            throw ex;
        }
    }

    @Override
    public void sendEmailFromAWSEmailSES(EmailNotificationDto emailNotificationDto) throws IOException, MessagingException {
        // Create a Properties object to contain connection configuration information.
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified properties.
        Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information.
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM,FROMNAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailNotificationDto.getTo()));
        msg.setSubject(SUBJECT);
        msg.setContent(BODY,"text/html");

        // Add a configuration set header. Comment or delete the
        // next line if you are not using a configuration set
        msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);

        // Create a transport.
        Transport transport = session.getTransport();

        // Send the message.
        try
        {
            System.out.println("Sending...");

            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
        }
        catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }
        finally
        {
            // Close and terminate the connection.
            transport.close();
        }
    }
}

