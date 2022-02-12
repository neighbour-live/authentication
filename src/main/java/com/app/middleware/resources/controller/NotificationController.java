package com.app.middleware.resources.controller;

import com.app.middleware.exceptions.ExceptionUtil;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserNotification;
import com.app.middleware.persistence.dto.EmailNotificationDto;
import com.app.middleware.persistence.mapper.UserNotificationMapper;
import com.app.middleware.persistence.request.UserNotificationRequest;
import com.app.middleware.persistence.response.GenericResponseEntity;
import com.app.middleware.persistence.response.PageableResponseEntity;
import com.app.middleware.persistence.response.PushNotificationResponse;
import com.app.middleware.resources.services.AuthorizationService;
import com.app.middleware.resources.services.EmailService;
import com.app.middleware.resources.services.NotificationService;
import com.app.middleware.utility.Constants;
import com.app.middleware.utility.StatusCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Value("${email.from}")
    private String emailFrom;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    //Current Demo
    @PostMapping
    @ApiOperation(value = "This operation is used to add user notification.")
    public ResponseEntity postUserNotification(@RequestBody UserNotificationRequest userNotificationRequest) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userNotificationRequest.getUserPublicId());
            UserNotification userNotification = notificationService.postUserNotification(userNotificationRequest, user);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserNotificationMapper.createUserNotificationDTOLazy(userNotification), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping
    @ApiOperation(value = "This operation is used to fetch specific user notification.")
    public ResponseEntity getUserNotification(@RequestParam("userPublicId") String userPublicId,
                                              @RequestParam("userNotificationPublicId") String userNotificationPublicId) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);
            UserNotification userNotification = notificationService.getUserNotification(userNotificationPublicId, user);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserNotificationMapper.createUserNotificationDTOLazy(userNotification), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PatchMapping("/read")
    @ApiOperation(value = "This operation is used to mark user notification as read/unread.")
    public ResponseEntity updateUserNotificationReadStatus(
            @RequestParam("userPublicId") String userPublicId,
            @RequestParam("userNotificationPublicId") String userNotificationPublicId,
            @RequestParam(value="markAsRead", required = false, defaultValue = "true") boolean markAsRead) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);

            if(markAsRead){
                notificationService.markUserNotificationsAsRead(userNotificationPublicId, user);
                return GenericResponseEntity.create(StatusCode.SUCCESS, "Marked as read.", HttpStatus.OK);
            } else {
                notificationService.markUserNotificationsAsUnread(userNotificationPublicId, user);
                return GenericResponseEntity.create(StatusCode.SUCCESS, "Marked as un-read.", HttpStatus.OK);
            }
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PatchMapping("/archive")
    @ApiOperation(value = "This operation is used to mark notification as archived.")
    public ResponseEntity updateUserNotificationArchiveStatus(
            @RequestParam("userPublicId") String userPublicId,
            @RequestParam("userNotificationPublicId") String userNotificationPublicId,
            @RequestParam(value="markAsArchived", required = false, defaultValue = "true") boolean markAsArchived) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);

            if(markAsArchived){
                notificationService.markUserNotificationsAsArchived(userNotificationPublicId, user);
                return GenericResponseEntity.create(StatusCode.SUCCESS, "Marked as archived.", HttpStatus.OK);
            } else {
                notificationService.markUserNotificationsAsUnarchived(userNotificationPublicId, user);
                return GenericResponseEntity.create(StatusCode.SUCCESS, "Marked as un-archived.", HttpStatus.OK);
            }
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }


    @PutMapping("/read-all")
    @ApiOperation(value = "This operation is used to mark all the notifications as read.")
    public ResponseEntity readUnreadUserNotification(
            @RequestParam("userPublicId") String userPublicId,
            @RequestParam(value="markAllAsRead", required = false, defaultValue = "true") boolean markAllAsRead) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);

            if(markAllAsRead){
                notificationService.markAllUserNotificationsAsRead(user);
                return GenericResponseEntity.create(StatusCode.SUCCESS, "All marked as read.", HttpStatus.OK);
            } else {
                notificationService.markAllUserNotificationsAsUnread(user);
                return GenericResponseEntity.create(StatusCode.SUCCESS, "All marked as un-read.", HttpStatus.OK);
            }
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PutMapping("/archive-all")
    @ApiOperation(value = "This operation is used mark all the notifications as archived.")
    public ResponseEntity archiveUnArchiveUserNotification(
            @RequestParam("userPublicId") String userPublicId,
            @RequestParam(value="markAllAsArchived", required = false, defaultValue = "true") boolean markAllAsArchived) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);
            if(markAllAsArchived){
                notificationService.markAllUserNotificationsAsArchived(user);
                return GenericResponseEntity.create(StatusCode.SUCCESS, "All marked as archived.", HttpStatus.OK);
            } else {
                notificationService.markAllUserNotificationsAsUnarchived(user);
                return GenericResponseEntity.create(StatusCode.SUCCESS, "All marked as un-archived.", HttpStatus.OK);
            }
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }




    @GetMapping("/page")
    @ApiOperation(value = "This operation is used to fetch user notifications by pagination.")
    public PageableResponseEntity<Object> getUserNotificationPage(@RequestParam("userPublicId") String userPublicId,
                                                                  @RequestParam(value = "pageNo") String pageNo,
                                                                  @RequestParam(value = "pageSize") String pageSize,
                                                                  @RequestParam(value="isNew", required = false, defaultValue = "false") boolean isNew,
                                                                  @RequestParam(value="isEarlier", required = false, defaultValue = "false") boolean isEarlier,
                                                                  @RequestParam(value="isArchived", required = false, defaultValue = "false") boolean isArchived) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);
            if(Integer.valueOf(pageNo) < 0 || Integer.valueOf(pageSize) < 0){
                throw new Exception("PageNo and PageSize must be positive numbers.");
            }

            if(!isNew && !isEarlier && !isArchived){
                throw new Exception("No notification type fetched.");
            } else if(isNew && isEarlier && isArchived){
                throw new Exception("Cannot fetch all notification types simultaneously.");
            } else if(isNew && isEarlier || isEarlier && isArchived || isNew && isArchived){
                throw new Exception("Cannot fetch two notification types simultaneously.");
            } else {
                if(isNew){
                    PageableResponseEntity<Object> pageableResponseEntity = notificationService.getUserNotificationsPageNew(Integer.valueOf(pageNo), Integer.valueOf(pageSize), user);
                    return pageableResponseEntity;
                } else if(isEarlier){
                    PageableResponseEntity<Object> pageableResponseEntity = notificationService.getUserNotificationsPageEarlier(Integer.valueOf(pageNo), Integer.valueOf(pageSize), user);
                    return pageableResponseEntity;
                } else if(isArchived){
                    PageableResponseEntity<Object> pageableResponseEntity = notificationService.getUserNotificationsPageArchived(Integer.valueOf(pageNo), Integer.valueOf(pageSize), user);
                    return pageableResponseEntity;
                }
            }
            
            return null;

        } catch (Exception e) {
            return ExceptionUtil.handlePaginatedException(e);
        }
    }

    @GetMapping("/email")
    @ApiOperation(value = "This hardcoded operation is used to send specific email.")
    public ResponseEntity sendEmailNotification() throws Exception {

        Map<String, String> placeHolders = new HashMap<String, String>();
        placeHolders.put("dynamic_url", "http://www.neighbour.live/verify-using-link");
        //sending Welcome Email
        EmailNotificationDto emailNotificationDto = EmailNotificationDto.builder()
                .to(emailFrom)
                .template(Constants.EmailTemplate.GENERIC_EMAIL.value())
                .placeHolders(placeHolders)
                .build();
        emailService.sendEmailFromExternalApi(emailNotificationDto);

        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }
}