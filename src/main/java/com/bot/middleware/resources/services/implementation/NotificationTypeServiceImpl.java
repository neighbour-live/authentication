package com.bot.middleware.resources.services.implementation;

import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.persistence.domain.NotificationType;
import com.bot.middleware.persistence.repository.NotificationTypeRepository;
import com.bot.middleware.persistence.type.NotificationEnum;
import com.bot.middleware.resources.services.NotificationTypeService;
import com.bot.middleware.utility.id.PublicIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationTypeServiceImpl implements NotificationTypeService {

    @Autowired
    NotificationTypeRepository notificationTypeRepository;

    @Override
    public NotificationType getNotificationTypeByName(String name) throws ResourceNotFoundException {
        NotificationType notificationType = notificationTypeRepository.getNotificationTypeByName(name);
        if(notificationType == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.NOTIFICATION_TYPE_NOT_FOUND_PUBLIC_ID, PublicIdGenerator.encodedPublicId(notificationType.getId()));
        return notificationType;
    }
}
