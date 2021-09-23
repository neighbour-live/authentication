package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill,Long> {
    Skill findByPublicId(Long decodePublicId);
}
