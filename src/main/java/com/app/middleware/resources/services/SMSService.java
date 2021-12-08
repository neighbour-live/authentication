package com.app.middleware.resources.services;

public interface SMSService {
     void sendOTPMessage(String otp, String phoneNumber) throws Exception;

     void sendOTPMessageByTwilio(String otp, String phoneNumber) throws Exception;
}
