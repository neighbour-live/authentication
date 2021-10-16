package com.app.middleware.persistence.repository;

import com.app.middleware.persistence.domain.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogsRepository extends JpaRepository<Log,Long> {
    List<Log> findAllByUserPublicId(Long decodePublicId);
}
