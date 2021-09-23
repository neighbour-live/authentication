package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.TaskTimeline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskTimelineRepository extends JpaRepository<TaskTimeline,Long> {
    TaskTimeline findByPublicId(Long decodePublicId);
}
