package com.bot.middleware.resources.services;

import com.bot.middleware.persistence.request.PushNotificationRequest;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface FCMService {
    void sendMessage(Map<String, String> data, PushNotificationRequest request) throws InterruptedException, ExecutionException;
    void sendMessageWithoutData(PushNotificationRequest request) throws InterruptedException, ExecutionException;
    void sendMessageToToken(PushNotificationRequest request) throws InterruptedException, ExecutionException;
}
