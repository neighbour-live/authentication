package com.app.middleware.persistence.repository;

import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserAward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAwardsRepository extends JpaRepository<UserAward,Long> {
    List<UserAward> findAllByUser(User user);
    
    UserAward findByPublicId(Long decodePublicId);
}
