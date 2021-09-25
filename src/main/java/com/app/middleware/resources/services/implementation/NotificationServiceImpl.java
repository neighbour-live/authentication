package com.app.middleware.resources.services.implementation;

import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.logging.GenericLog;
import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.persistence.domain.FirebaseLog;
import com.app.middleware.persistence.domain.NotificationType;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserNotification;
import com.app.middleware.persistence.dto.EmailNotificationDto;
import com.app.middleware.persistence.dto.SubscriptionRequestDTO;
import com.app.middleware.persistence.dto.UserNotificationDTO;
import com.app.middleware.persistence.mapper.UserNotificationMapper;
import com.app.middleware.persistence.repository.FirebaseLogRepository;
import com.app.middleware.persistence.repository.UserNotificationRepository;
import com.app.middleware.persistence.repository.UserRepository;
import com.app.middleware.persistence.request.PushNotificationRequest;
import com.app.middleware.persistence.request.UserNotificationRequest;
import com.app.middleware.persistence.response.PageableResponseEntity;
import com.app.middleware.persistence.type.RequestStatus;
import com.app.middleware.resources.services.EmailService;
import com.app.middleware.resources.services.FCMService;
import com.app.middleware.resources.services.NotificationService;
import com.app.middleware.resources.services.NotificationTypeService;
import com.app.middleware.utility.Constants;
import com.app.middleware.utility.StatusCode;
import com.app.middleware.utility.id.PublicIdGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.google.gson.Gson;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private GenericLog log;

    @Autowired
    private NotificationTypeService notificationTypeService;

    @Autowired
    private FirebaseLogRepository firebaseLogRepository;

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Value("${spring.profiles.active}")
    private String env;

    private final String TYPE = "type";
    private final String CONTENT = "content";
    private final String MESSAGE = "message";
    private final String IMAGE = "image";
    private final String BOT_TOPIC_PREFIX = "Bot-broadcast-";
    private String getBotTopic() {
        return BOT_TOPIC_PREFIX + env;
    }

    /**
     * Current PUSH NOTIFICATION functions Start here
     * */
    private Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private FirebaseApp firebaseApp;

    @Value("${firebase.config}")
    private String firebaseConfig;

    @Autowired
    private FCMService fcmService;


    @PostConstruct
    private void initialize() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseConfig).getInputStream())).build();

            if (FirebaseApp.getApps().isEmpty()) {
                this.firebaseApp = FirebaseApp.initializeApp(options);
            } else {
                this.firebaseApp = FirebaseApp.getInstance();
            }
        } catch (IOException e) {
            log.error("Create FirebaseApp Error", e);
        }
    }


    public NotificationServiceImpl(FCMService fcmService) {
        this.fcmService = fcmService;
    }

    public void sendPushNotification(PushNotificationRequest request) {
        try {
            fcmService.sendMessage(getSamplePayloadData(), request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void sendPushNotificationWithoutData(PushNotificationRequest request) {
        try {
            fcmService.sendMessageWithoutData(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }


    public void sendPushNotificationToToken(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToToken(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    private Map<String, String> getSamplePayloadData() {
        Map<String, String> pushData = new HashMap<>();
        pushData.put("messageId", "msgId");
        pushData.put("text", "text");
        pushData.put("user", "BidOnTask");
        return pushData;
    }

    @Override
    public String sendPnsToTopic(UserNotificationRequest userNotificationRequestDto) {
        Message message = Message.builder()
                .setTopic(userNotificationRequestDto.getTarget())
                .setNotification(new Notification(userNotificationRequestDto.getTitle(), userNotificationRequestDto.getBody()))
                .putData("content", userNotificationRequestDto.getTitle())
                .putData("body", userNotificationRequestDto.getBody())
                .build();

        String response = null;
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("Fail to send firebase notification", e);
        }

        return response;
    }

    @Override
    public String sendPnsToDevice(UserNotificationRequest userNotificationRequestDto) {
        Message message = Message.builder()
                .setToken(userNotificationRequestDto.getFirebaseKey())
                .setNotification(new Notification(userNotificationRequestDto.getTitle(), userNotificationRequestDto.getBody()))
                .putData("content", userNotificationRequestDto.getTitle())
                .putData("body", userNotificationRequestDto.getBody())
                .build();

        String response = null;
        try {
            response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("Fail to send firebase notification", e);
        }

        return response;
    }

    @Override
    public void unsubscribeFromTopic(SubscriptionRequestDTO subscriptionRequestDto) {
        try {
            FirebaseMessaging.getInstance(firebaseApp).unsubscribeFromTopic(subscriptionRequestDto.getTokens(),
                    subscriptionRequestDto.getTopicName());
        } catch (FirebaseMessagingException e) {
            log.error("Firebase unsubscribe from topic fail", e);
        }
    }

    @Override
    public void subscribeToTopic(SubscriptionRequestDTO subscriptionRequestDto) {
        try {
            FirebaseMessaging.getInstance(firebaseApp).subscribeToTopic(subscriptionRequestDto.getTokens(),
                    subscriptionRequestDto.getTopicName());
        } catch (FirebaseMessagingException e) {
            log.error("Firebase subscribe to topic fail", e);
        }
    }


    /**
     * current PUSH NOTIFICATION functions end here
     * */







    @Override
    public void sendSync(User user, String registrationToken, NotificationType notificationType, String message, Map<String, ?> contentMap) {
        sendNotification(user, registrationToken, notificationType, message, contentMap);
    }

    @Async
    @Override
    public void sendAsync(User user, String registrationToken, NotificationType notificationType, String message, Map<String, ?> contentMap) {
        sendNotification(user, registrationToken, notificationType, message, contentMap);
    }

    @Async
    @Override
    public void sendAsync(UserNotification userNotification) {
        sendNotification(userNotification,
                userNotification.getUser(),
                userNotification.getUser().getFirebaseKey(),
                userNotification.getNotificationType(),
                userNotification.getNotificationText(),
                userNotification.getNotificationImage(),
                userNotification.getActionsInfo());
    }

    @Override
    public void sendSync(UserNotification userNotification) {
        sendNotification(userNotification,
                userNotification.getUser(),
                userNotification.getUser().getFirebaseKey(),
                userNotification.getNotificationType(),
                userNotification.getNotificationText(),
                userNotification.getNotificationImage(),
                userNotification.getActionsInfo());
    }

    @Async
    @Override
    public void sendAsync(List<UserNotification> userNotifications) {
        for (UserNotification userNotification : userNotifications) {
            sendAsync(userNotification);
        }
    }

    @Override
    public void sendSync(List<UserNotification> userNotifications) {
        for (UserNotification userNotification : userNotifications) {
            sendSync(userNotification);
        }
    }

    @Override
    public void broadcastSync(List<UserNotification> userNotifications) {
        broadcastNotification(userNotifications);
    }

    @Async
    @Override
    public void broadcastAsync(List<UserNotification> userNotifications) {
        broadcastNotification(userNotifications);
    }

    private void sendNotification(User user, String registrationToken, NotificationType notificationType, String message, Map<String, ?> contentMap) {
        String content = contentMap == null ? null : new Gson().toJson(contentMap);
        sendNotification(null, user, registrationToken, notificationType, message, null,content);
    }


    private Notification getNotificationForFirebase(String env, UserNotification userNotification) {
        return null;
//        return Notification
//                .builder()
//                .setTitle(getNotificationTitle(env))
//                .setBody(userNotification.getNotificationText())
//                .setImage(userNotification.getNotificationImage())
//                .build();
    }

    private String getNotificationTitle(String env) {
        return env.equals("prod") ? "Bot" : "Bot - " + StringUtils.capitalize(env);
    }

    private ApnsConfig getApnsConfig(UserNotification userNotification, Map<String, String> notificationMap) {
        ApsAlert apsAlert = ApsAlert.builder()
                .setBody(userNotification.getNotificationText())
                .setTitle(getNotificationTitle(env))
                .build();
        Aps aps = Aps.builder()
                .setAlert(apsAlert)
                .setSound("default")
                .build();
        return ApnsConfig.builder()
                .setAps(aps)
                .putAllCustomData(new HashMap<>(notificationMap))
                .build();
    }


    private void sendNotification(UserNotification userNotification, User user, String registrationToken, NotificationType notificationType, String message, String image, String contentString) {

        Map<String, String> notificationMap = getNotificationMap(notificationType, message, image, contentString);

        if (StringUtils.isEmpty(registrationToken)) {
            FirebaseLog firebaseLog = FirebaseLog.builder()
                    .notificationType(notificationType)
                    .userNotification(userNotification)
                    .payload(new Gson().toJson(notificationMap))
                    .requestId(log.getLogID())
                    .user(user)
                    .build();
            errorFirebaseNotification(firebaseLog, "Registration token is NULL");
            return;
        }

        FirebaseLog firebaseLog = createFirebaseNotification(userNotification, notificationType, new Gson().toJson(notificationMap), log.getLogID(), user);
        try {
            ApnsConfig apnsConfig = getApnsConfig(userNotification, notificationMap);
            Message firebaseNotificationMessage = Message.builder()
                    .setApnsConfig(apnsConfig)
                    .putAllData(notificationMap)
                    .setToken(registrationToken)
                    .build();
            String messageId = FirebaseMessaging.getInstance().send(firebaseNotificationMessage);
            successFirebaseNotification(firebaseLog);
            log.info(String.format("Firebase Notification Sent! Message Id: %s ||| Notification Type: %s ||| Notification Content: %s", messageId, notificationType.getName(), contentString));
        } catch (FirebaseMessagingException e) {
            errorFirebaseNotification(firebaseLog, e.getMessage());
            log.error("Unable to send Firebase Push Notification. Error: ", e);
        }
    }

    private void broadcastNotification(List<UserNotification> userNotifications) {
        try {
            if (CollectionUtils.isNotEmpty(userNotifications)) {
                String BotTopic = getBotTopic();
                log.cronLogger("Sending Message to topic: " + BotTopic);
                subscribeToTopic(userNotifications.stream().map(UserNotification::getUser).collect(Collectors.toList()), BotTopic);

                UserNotification userNotification = userNotifications.get(0);
                Map<String, String> notificationMap = new HashMap<>();
                notificationMap.put(TYPE, userNotification.getNotificationType().getName());
                notificationMap.put(MESSAGE, userNotification.getNotificationText());
                notificationMap.put(CONTENT, userNotification.getActionsInfo());

                ApnsConfig apnsConfig = getApnsConfig(userNotification, notificationMap);

                Message message = Message.builder()
                        .setApnsConfig(apnsConfig)
                        .putAllData(notificationMap)
                        .setTopic(BotTopic)
                        .build();
                FirebaseMessaging.getInstance().send(message);
            }
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> getNotificationMap(NotificationType notificationType, String message, String image, String content) {
        Map<String, String> notificationMap = new HashMap<>();
        notificationMap.put(TYPE, notificationType.getName());
        notificationMap.put(MESSAGE, message);
        notificationMap.put(CONTENT, content);
        notificationMap.put(IMAGE, image);
        return notificationMap;
    }

    private void subscribeToTopic(List<User> users, String topic) throws FirebaseMessagingException {
        List<String> firebaseKeys = users.stream()
                .map(User::getFirebaseKey)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());
        int step = 1000;
        for (int i = 0; i < firebaseKeys.size(); i += step) {
            List<String> keysSubset = firebaseKeys.stream().skip(i).limit(step).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(keysSubset)) {
                FirebaseMessaging.getInstance().subscribeToTopic(keysSubset, topic);
            }
        }
    }

    private String convertToString(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private FirebaseLog createFirebaseNotification(UserNotification userNotification, NotificationType notificationType, String payload, String requestId, User user) {
        return firebaseLogRepository.save(new FirebaseLog(userNotification, notificationType, payload, requestId, user));
    }

    private FirebaseLog successFirebaseNotification(FirebaseLog firebase) {
        firebase.setRequestStatus(RequestStatus.SUCCESS);
        return firebaseLogRepository.save(firebase);
    }

    private FirebaseLog errorFirebaseNotification(FirebaseLog firebase, String errorMsg) {
        firebase.setRequestStatus(RequestStatus.FAILED);
        firebase.setErrorMsg(errorMsg);
        return firebaseLogRepository.save(firebase);
    }


    //CRUD OPERATIONS
    @Override
    public UserNotification postUserNotification(UserNotificationRequest notificationRequest, User user) throws ResourceNotFoundException {

        NotificationType notificationType = notificationTypeService.getNotificationTypeByName(notificationRequest.getNotificationType());

        UserNotification userNotification = new UserNotification();
        userNotification.setNotificationImage(notificationRequest.getImageUrl());
        userNotification.setNotificationTitle(notificationRequest.getTitle());
        userNotification.setNotificationText(notificationRequest.getBody());
        userNotification.setActions(notificationRequest.getActions());
        userNotification.setActionsInfo(notificationRequest.getActionsInfo().toString());
        userNotification.setUser(user);
        userNotification.setPublicId(PublicIdGenerator.generatePublicId());
        userNotification.setNotificationType(notificationType);
        userNotification.setIsArchived(false);
        userNotification.setIsRead(false);
        userNotification.setIsDeleted(false);

        String response = sendPnsToDevice(notificationRequest);
        log.info(response);

        userNotification = userNotificationRepository.save(userNotification);
        return userNotification;
    }

    @Override
    public UserNotification getUserNotification(String userNotificationPublicId, User user) throws ResourceNotFoundException {
        UserNotification userNotification = userNotificationRepository.findByPublicId(PublicIdGenerator.decodePublicId(userNotificationPublicId));
        if(userNotification == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.NOTIFICATION_NOT_FOUND_PUBLIC_ID, userNotificationPublicId);
        return userNotification;
    }

    @Override
    public PageableResponseEntity<Object> getUserNotificationsPage(Integer pageNo, Integer pageSize, User user) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createDateTime").descending());
        Page<UserNotification> userNotificationsPage = userNotificationRepository.getUserNotificationsPage(pageable, user);
        List<UserNotificationDTO> userNotificationDTOList = UserNotificationMapper.createUserNotificationDTOListLazy(userNotificationsPage.getContent());
        PageableResponseEntity<Object> pageableResponseEntity = new PageableResponseEntity<Object>(
                StatusCode.SUCCESS,
                "Paginated response",
                userNotificationDTOList,
                userNotificationsPage.getTotalElements(),
                userNotificationsPage.getSize(),
                userNotificationsPage.getTotalPages(),
                0
        );

        return pageableResponseEntity;
    }

    @Override
    public PageableResponseEntity<Object> getUserNotificationsPageNew(Integer pageNo, Integer pageSize, User user) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createDateTime").descending());
        ZonedDateTime toDate = ZonedDateTime.now();
        ZonedDateTime middleDate = toDate.minusDays(1);
        ZonedDateTime fromDate = toDate.minusDays(2);
        Integer unreadCount = userNotificationRepository.getUserNotificationsNewUnreadCount(user, fromDate, toDate);
        Page<UserNotification> userNotificationsPage = userNotificationRepository.getUserNotificationsPageNew(pageable, user, fromDate, toDate);
        List<UserNotificationDTO> userNotificationDTOList = UserNotificationMapper.createUserNotificationDTOListLazy(userNotificationsPage.getContent());
        PageableResponseEntity<Object> pageableResponseEntity = new PageableResponseEntity<Object>(
                StatusCode.SUCCESS,
                "Paginated response",
                userNotificationDTOList,
                userNotificationsPage.getTotalElements(),
                userNotificationsPage.getSize(),
                userNotificationsPage.getTotalPages(),
                unreadCount
        );

        return pageableResponseEntity;
    }

    @Override
    public PageableResponseEntity<Object> getUserNotificationsPageEarlier(Integer pageNo, Integer pageSize, User user) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createDateTime").descending());
        ZonedDateTime toDate = ZonedDateTime.now();
        ZonedDateTime middleDate = toDate.minusDays(1);
        ZonedDateTime fromDate = toDate.minusDays(2);
        Integer unreadCount = userNotificationRepository.getUserNotificationsEarlierUnreadCount(user, fromDate);
        Page<UserNotification> userNotificationsPage = userNotificationRepository.getUserNotificationsPageEarlier(pageable, user, fromDate);
        List<UserNotificationDTO> userNotificationDTOList = UserNotificationMapper.createUserNotificationDTOListLazy(userNotificationsPage.getContent());
        PageableResponseEntity<Object> pageableResponseEntity = new PageableResponseEntity<Object>(
                StatusCode.SUCCESS,
                "Paginated response",
                userNotificationDTOList,
                userNotificationsPage.getTotalElements(),
                userNotificationsPage.getSize(),
                userNotificationsPage.getTotalPages(),
                unreadCount
        );

        return pageableResponseEntity;
    }

    @Override
    public PageableResponseEntity<Object> getUserNotificationsPageArchived(Integer pageNo, Integer pageSize, User user) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createDateTime").descending());
        Integer unreadCount = userNotificationRepository.getUserNotificationsArchivedUnreadCount(user);
        Page<UserNotification> userNotificationsPage = userNotificationRepository.getUserNotificationsPageArchived(pageable, user);
        List<UserNotificationDTO> userNotificationDTOList = UserNotificationMapper.createUserNotificationDTOListLazy(userNotificationsPage.getContent());
        PageableResponseEntity<Object> pageableResponseEntity = new PageableResponseEntity<Object>(
                StatusCode.SUCCESS,
                "Paginated response",
                userNotificationDTOList,
                userNotificationsPage.getTotalElements(),
                userNotificationsPage.getSize(),
                userNotificationsPage.getTotalPages(),
                unreadCount
        );

        return pageableResponseEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void markAllUserNotificationsAsRead(User user) {
        List<UserNotification> userNotifications =  userNotificationRepository.findAllUserUnreadNotifications(user);
        userNotifications.forEach(
                userNotification -> userNotification.setIsRead(true)
        );
        userNotificationRepository.saveAll(userNotifications);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void markAllUserNotificationsAsUnread(User user) {
        List<UserNotification> userNotifications =  userNotificationRepository.findAllUserReadNotifications(user);
        userNotifications.forEach(
                userNotification -> userNotification.setIsRead(false)
        );
        userNotificationRepository.saveAll(userNotifications);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void markAllUserNotificationsAsArchived(User user) {
        List<UserNotification> userNotifications =  userNotificationRepository.findAllUserUnArchivedNotifications(user);
        userNotifications.forEach(
                userNotification -> userNotification.setIsArchived(true)
        );
        userNotificationRepository.saveAll(userNotifications);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void markAllUserNotificationsAsUnarchived(User user) {
        List<UserNotification> userNotifications =  userNotificationRepository.findAllUserArchivedNotifications(user);
        userNotifications.forEach(
                userNotification -> userNotification.setIsArchived(false)
        );
        userNotificationRepository.saveAll(userNotifications);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void markUserNotificationsAsRead(String userNotificationPublicId, User user) throws ResourceNotFoundException {
        UserNotification userNotification = userNotificationRepository.findByPublicId(PublicIdGenerator.decodePublicId(userNotificationPublicId));
        if(userNotification == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.NOTIFICATION_NOT_FOUND_PUBLIC_ID, userNotificationPublicId);
        userNotification.setIsRead(true);
        userNotificationRepository.save(userNotification);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void markUserNotificationsAsUnread(String userNotificationPublicId, User user) throws ResourceNotFoundException {
        UserNotification userNotification = userNotificationRepository.findByPublicId(PublicIdGenerator.decodePublicId(userNotificationPublicId));
        if(userNotification == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.NOTIFICATION_NOT_FOUND_PUBLIC_ID, userNotificationPublicId);
        userNotification.setIsRead(false);
        userNotificationRepository.save(userNotification);
    }

    @Override
    public void markUserNotificationsAsArchived(String userNotificationPublicId, User user) throws ResourceNotFoundException {
        UserNotification userNotification = userNotificationRepository.findByPublicId(PublicIdGenerator.decodePublicId(userNotificationPublicId));
        if(userNotification == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.NOTIFICATION_NOT_FOUND_PUBLIC_ID, userNotificationPublicId);
        userNotification.setIsArchived(true);
        userNotificationRepository.save(userNotification);
    }

    @Override
    public void markUserNotificationsAsUnarchived(String userNotificationPublicId, User user) throws ResourceNotFoundException {
        UserNotification userNotification = userNotificationRepository.findByPublicId(PublicIdGenerator.decodePublicId(userNotificationPublicId));
        if(userNotification == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.NOTIFICATION_NOT_FOUND_PUBLIC_ID, userNotificationPublicId);
        userNotification.setIsArchived(false);
        userNotificationRepository.save(userNotification);
    }

    // Run at Daily 7 pm
//    @Scheduled(cron = "0 19 * * * ?")
//    @Scheduled(cron = "*/5 * * * * ?")
    public void sendEmailVerificationTemplateReminder() {

        List<User> users = userRepository.findByEmailVerified(false);

        users.forEach(user -> {
            //sending Verification Email
            try {
                emailService.sendEmailFromExternalApi(EmailNotificationDto.builder()
                        .to(user.getEmail())
                        .template(Constants.EmailTemplate.EMAIL_VERIFICATION_TEMPLATE.value())
                        .build());
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        });
    }

    // Run at Daily 8 pm
//    @Scheduled(cron = "0 20 * * * ?")
    public void sendCardVerificationTemplateReminder() {

//        List<User> users = userRepository.findByCardVerified(false);
//
//        users.forEach(user -> {
//            //sending Verification Email
//            try {
//                emailService.sendEmailFromExternalApi(EmailNotificationDto.builder()
//                        .to(user.getEmail())
//                        .template(Constants.EmailTemplate.TEST_TEMPLATE.value())
//                        .build());
//            } catch (IOException e) {
//                e.printStackTrace();
//                logger.error(e.getMessage());
//            }
//        });
    }

    // Run at Daily 9 pm
//    @Scheduled(cron = "0 21 * * * ?")
    public void sendSterlingVerificationTemplateReminder() {

//        List<User> users = userRepository.findByCardVerified(false);
//
//        users.forEach(user -> {
//            //sending Verification Email
//            try {
//                emailService.sendEmailFromExternalApi(EmailNotificationDto.builder()
//                        .to(user.getEmail())
//                        .template(Constants.EmailTemplate.TEST_TEMPLATE.value())
//                        .build());
//            } catch (IOException e) {
//                e.printStackTrace();
//                logger.error(e.getMessage());
//            }
//        });
    }

    // Run at Daily 10 pm
//    @Scheduled(cron = "0 22 * * * ?")
    public void sendPhoneVerificationTemplateReminder() {

//        List<User> users = userRepository.findByCardVerified(false);
//
//        users.forEach(user -> {
//            //sending Verification Email
//            try {
//                emailService.sendEmailFromExternalApi(EmailNotificationDto.builder()
//                        .to(user.getEmail())
//                        .template(Constants.EmailTemplate.TEST_TEMPLATE.value())
//                        .build());
//            } catch (IOException e) {
//                e.printStackTrace();
//                logger.error(e.getMessage());
//            }
//        });
    }


}
