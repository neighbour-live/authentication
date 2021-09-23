package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.SupportUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportUserRepository extends JpaRepository<SupportUser,Long> {
    SupportUser findByPublicId(Long decodePublicId);
}
