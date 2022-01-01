package com.app.middleware.resources.services.implementation;

import com.app.middleware.exceptions.ExceptionUtil;
import com.app.middleware.resources.services.SMSService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SMSServiceImpl implements SMSService {

    // Find your Account Sid and Auth Token at twilio.com/console
    @Value("${twilio-sid}")
    private String ACCOUNT_SID;

    @Value("${twilio-token}")
    private String AUTH_TOKEN;

    @Value("${twilio-sender}")
    private String SMS_FROM;

    @Override
    public void sendOTPMessageByTwilio(String otp, String phoneNumber) throws Exception {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message
                    .creator(new PhoneNumber("+"+phoneNumber), new PhoneNumber(SMS_FROM), "Neighbour Live:\nYour One Time Password(OTP) is: " + otp)
                    .create();
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

    @Override
    public void sendOTPMessage(String otp, String phoneNumber) throws Exception {
        System.out.println("\nPHONE OTP: " + otp + ", Phone Number: " + phoneNumber + "\n");
    }
}
