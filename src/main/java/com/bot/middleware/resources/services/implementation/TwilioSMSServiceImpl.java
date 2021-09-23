package com.bot.middleware.resources.services.implementation;
import com.bot.middleware.exceptions.ExceptionUtil;
import com.bot.middleware.exceptions.type.BaseException;
import com.bot.middleware.resources.services.SMSService;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class TwilioSMSServiceImpl implements SMSService {

    // Find your Account Sid and Auth Token at twilio.com/console
    private static final String ACCOUNT_SID = "ACc494b70614e9e528292d985c7fde46ef";
    private static final String AUTH_TOKEN = "b020fbf5b8f8a3be84bf3345418b76a8";
    private static final String SMS_FROM = "+13312085408";




    @Override
    public void sendOTPMessageByTwilio(String otp, String phoneNumber) throws Exception {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message
                    .creator(new PhoneNumber("+" + phoneNumber), // to
                            new PhoneNumber(SMS_FROM), // from
                            "BidOnTask \n" +
                                    "\n" +
                                    "Your One Time Password(OTP) is: " + otp)
                    .create();
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }
}
