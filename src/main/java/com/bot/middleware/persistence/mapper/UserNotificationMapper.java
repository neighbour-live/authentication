package com.bot.middleware.persistence.mapper;

import com.bot.middleware.persistence.domain.Award;
import com.bot.middleware.persistence.domain.UserNotification;
import com.bot.middleware.persistence.dto.AwardDTO;
import com.bot.middleware.persistence.dto.UserNotificationDTO;
import com.bot.middleware.utility.id.PublicIdGenerator;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserNotificationMapper {

    public static UserNotificationDTO createUserNotificationDTOLazy(UserNotification userNotification) {
        UserNotificationDTO userNotificationDTO = UserNotificationDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(userNotification.getPublicId()))
                .imageUrl(userNotification.getNotificationImage())
                .title(userNotification.getNotificationTitle())
                .body(userNotification.getNotificationText())
                .isDeleted(userNotification.getIsDeleted())
                .isArchived(userNotification.getIsArchived())
                .isRead(userNotification.getIsRead())
                .notificationType(userNotification.getNotificationType().toString())
                .actions((userNotification.getActions() == null) ? "": userNotification.getActions())
                .actionsInfo((userNotification.getActionsInfo() == null) ? "": userNotification.getActionsInfo())
                .createDateTime(userNotification.getCreateDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        return userNotificationDTO;
    }

    public static List<UserNotificationDTO> createUserNotificationDTOListLazy(Collection<UserNotification> userNotifications) {
        List<UserNotificationDTO> userNotificationDTOS = new ArrayList<>();
        userNotifications.forEach(userNotification -> userNotificationDTOS.add(createUserNotificationDTOLazy(userNotification)));
        return userNotificationDTOS;
    }
}
