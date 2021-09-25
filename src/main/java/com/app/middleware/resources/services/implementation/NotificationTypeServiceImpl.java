package com.app.middleware.resources.services.implementation;

import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.persistence.domain.NotificationType;
import com.app.middleware.persistence.repository.NotificationTypeRepository;
import com.app.middleware.resources.services.NotificationTypeService;
import com.app.middleware.utility.id.PublicIdGenerator;
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
