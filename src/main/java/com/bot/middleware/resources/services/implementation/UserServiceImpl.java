package com.bot.middleware.resources.services.implementation;

import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.repository.UserRepository;
import com.bot.middleware.resources.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<User> getFeaturedTaskers(String city, Pageable pageable) {
        Page<User> usersPage = userRepository.getAllFeaturedTaskers(city, pageable);
        return usersPage;
    }

    @Override
    public User save(User user) {
       return userRepository.save(user);
    }

    @Override
    public User findByPublicId(Long decodePublicId) {
        return userRepository.findByPublicId(decodePublicId);
    }
}
