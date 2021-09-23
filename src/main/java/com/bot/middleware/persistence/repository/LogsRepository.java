package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.Award;
import com.bot.middleware.persistence.domain.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogsRepository extends JpaRepository<Log,Long> {
    List<Log> findAllByUserPublicId(Long decodePublicId);
}
