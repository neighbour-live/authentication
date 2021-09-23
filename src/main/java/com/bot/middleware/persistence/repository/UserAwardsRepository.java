package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserAward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAwardsRepository extends JpaRepository<UserAward,Long> {
    List<UserAward> findAllByUser(User user);
    
    UserAward findByPublicId(Long decodePublicId);
}
