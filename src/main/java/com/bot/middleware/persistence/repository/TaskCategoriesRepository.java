package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.TaskCategories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskCategoriesRepository extends JpaRepository<TaskCategories,Long> {
    TaskCategories findByPublicId(Long decodePublicId);
}
