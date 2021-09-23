package com.bot.middleware.resources.services;

import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserTemporary;

import java.time.ZonedDateTime;
import java.util.Collection;

public interface UserTemporaryService {

    UserTemporary findByUser(User user) throws ResourceNotFoundException;

    UserTemporary findByPhoneNumber(String phoneNumber) throws ResourceNotFoundException;

    UserTemporary findByEmailIgnoreCase(String email) throws ResourceNotFoundException;

    UserTemporary createTempUser(User user);

    UserTemporary save(UserTemporary userTemporary);

    Collection<UserTemporary> findAllByUpdateDateTimeBefore(ZonedDateTime updateDateTime);
}
