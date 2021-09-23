package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.Award;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AwardRepository extends JpaRepository<Award,Long> {
    Award findByPublicId(Long decodePublicId);
}
