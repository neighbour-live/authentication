package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.FirebaseLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FirebaseLogRepository extends JpaRepository<FirebaseLog, Long> {
}
