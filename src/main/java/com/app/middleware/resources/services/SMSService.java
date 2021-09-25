package com.app.middleware.resources.services;

public interface SMSService {
     void sendOTPMessage(String otp, String phoneNumber) throws Exception;
}
