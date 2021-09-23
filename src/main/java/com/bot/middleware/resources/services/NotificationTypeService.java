package com.bot.middleware.resources.services;

import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.persistence.domain.NotificationType;
import org.springframework.stereotype.Service;

@Service
public interface NotificationTypeService {
    NotificationType getNotificationTypeByName(String name) throws ResourceNotFoundException;
}
