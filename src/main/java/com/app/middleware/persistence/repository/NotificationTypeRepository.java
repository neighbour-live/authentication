package com.app.middleware.persistence.repository;

import com.app.middleware.persistence.domain.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {
    NotificationType getNotificationTypeByName(String name);
}
