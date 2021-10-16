package com.app.middleware.persistence.repository;

import com.app.middleware.persistence.domain.FirebaseLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FirebaseLogRepository extends JpaRepository<FirebaseLog, Long> {
}
