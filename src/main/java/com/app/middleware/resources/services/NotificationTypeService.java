package com.app.middleware.resources.services;

import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.persistence.domain.NotificationType;
import org.springframework.stereotype.Service;

@Service
public interface NotificationTypeService {
    NotificationType getNotificationTypeByName(String name) throws ResourceNotFoundException;
}
