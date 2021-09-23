package com.bot.middleware.resources.services;

import com.bot.middleware.persistence.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<User> getFeaturedTaskers(String city, Pageable pageable);

    User save(User user);

    User findByPublicId(Long decodePublicId);
}
