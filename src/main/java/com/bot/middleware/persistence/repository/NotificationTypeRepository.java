package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {
    NotificationType getNotificationTypeByName(String name);
}
