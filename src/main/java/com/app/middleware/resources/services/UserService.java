package com.app.middleware.resources.services;

import com.app.middleware.persistence.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    User save(User user);

    User findByPublicId(Long decodePublicId);
}
