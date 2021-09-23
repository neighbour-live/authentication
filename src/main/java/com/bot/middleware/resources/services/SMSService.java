package com.bot.middleware.resources.services;

public interface SMSService {
     void sendOTPMessageByTwilio(String otp, String phoneNumber) throws Exception;
}
