package com.app.middleware.resources.services.implementation;

import com.app.middleware.resources.services.SMSService;
import org.springframework.stereotype.Service;

@Service
public class SMSServiceImpl implements SMSService {
    @Override
    public void sendOTPMessage(String otp, String phoneNumber) throws Exception {
        System.out.println("\nPHONE OTP: " + otp + ", Phone Number: " + phoneNumber + "\n");
    }
}
