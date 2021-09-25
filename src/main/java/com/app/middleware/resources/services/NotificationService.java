package com.app.middleware.resources.services;

import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.exceptions.type.SomethingUnexpectedException;
import com.app.middleware.persistence.domain.NotificationType;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserNotification;
import com.app.middleware.persistence.request.UserNotificationRequest;
import com.app.middleware.persistence.dto.SubscriptionRequestDTO;
import com.app.middleware.persistence.request.PushNotificationRequest;
import com.app.middleware.persistence.response.PageableResponseEntity;

import java.util.List;
import java.util.Map;

public interface NotificationService {

    //CRUD OPERATIONS
    UserNotification postUserNotification(UserNotificationRequest notification, User user) throws ResourceNotFoundException;

    UserNotification getUserNotification(String userNotificationPublicId, User user) throws ResourceNotFoundException;

    PageableResponseEntity<Object> getUserNotificationsPage(Integer pageNo, Integer pageSize, User user);

    PageableResponseEntity<Object> getUserNotificationsPageNew(Integer pageNo, Integer pageSize, User user);

    PageableResponseEntity<Object> getUserNotificationsPageEarlier(Integer pageNo, Integer pageSize, User user);

    PageableResponseEntity<Object> getUserNotificationsPageArchived(Integer pageNo, Integer pageSize, User user);

    void markAllUserNotificationsAsRead(User user);

    void markAllUserNotificationsAsUnread(User user);

    void markAllUserNotificationsAsArchived(User user);

    void markAllUserNotificationsAsUnarchived(User user);

    void markUserNotificationsAsRead(String userNotificationPublicId, User user) throws ResourceNotFoundException;

    void markUserNotificationsAsUnread(String userNotificationPublicId, User user) throws ResourceNotFoundException;

    void markUserNotificationsAsArchived(String userNotificationPublicId, User user) throws ResourceNotFoundException;

    void markUserNotificationsAsUnarchived(String userNotificationPublicId, User user) throws ResourceNotFoundException;

    //CURRENT PUSH NOTIFICATION SERVICE CODE
    void sendPushNotificationToToken(PushNotificationRequest request);

    void sendPushNotificationWithoutData(PushNotificationRequest request);

    void sendPushNotification(PushNotificationRequest request);

    String sendPnsToTopic(UserNotificationRequest userNotificationRequestDto);

    String sendPnsToDevice(UserNotificationRequest userNotificationRequestDto);

    void unsubscribeFromTopic(SubscriptionRequestDTO subscriptionRequestDto);

    void subscribeToTopic(SubscriptionRequestDTO subscriptionRequestDto);

    //OLD FIREBASE SERVICE CODE
    void sendSync(User user, String registrationToken, NotificationType notificationType, String message, Map<String, ?> contentMap) throws SomethingUnexpectedException;

    void sendAsync(User user, String registrationToken, NotificationType notificationType, String message, Map<String, ?> contentMap) throws SomethingUnexpectedException;

    void sendAsync(UserNotification userNotification);

    void sendSync(UserNotification userNotification);

    void sendAsync(List<UserNotification> userNotifications);

    void sendSync(List<UserNotification> userNotifications);

    void broadcastSync(List<UserNotification> userNotifications);

    void broadcastAsync(List<UserNotification> userNotifications);
}
