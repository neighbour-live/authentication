package com.app.middleware.resources.services.implementation;

import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.repository.UserRepository;
import com.app.middleware.resources.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User user) {
       return userRepository.save(user);
    }

    @Override
    public User findByPublicId(Long decodePublicId) {
        return userRepository.findByPublicId(decodePublicId);
    }
}
