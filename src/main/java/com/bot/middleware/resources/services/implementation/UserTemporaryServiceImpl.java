package com.bot.middleware.resources.services.implementation;

import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserTemporary;
import com.bot.middleware.persistence.repository.UserTempRepository;
import com.bot.middleware.resources.services.UserTemporaryService;
import com.bot.middleware.utility.id.PublicIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Collection;

@Service
public class UserTemporaryServiceImpl implements UserTemporaryService {

    @Autowired
    private UserTempRepository userTempRepository;

    @Override
    public UserTemporary findByUser(User user) throws ResourceNotFoundException {
        UserTemporary userTemporary =  userTempRepository.findByUser(user);
        if(userTemporary == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_TEMPORARY_NOT_FOUND_WITH_PUBLIC_ID, PublicIdGenerator.encodedPublicId(user.getPublicId()));
        return  userTemporary;
    }

    @Override
    public UserTemporary findByPhoneNumber(String phoneNumber) throws ResourceNotFoundException {
        UserTemporary userTemporary =  userTempRepository.findByPhoneNumber(phoneNumber);
        if(userTemporary == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_TEMPORARY_NOT_FOUND_WITH_PUBLIC_ID, phoneNumber);
        return  userTemporary;
    }

    @Override
    public UserTemporary findByEmailIgnoreCase(String email) throws ResourceNotFoundException {
        UserTemporary userTemporary =  userTempRepository.findByEmailIgnoreCase(email);
        if(userTemporary == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_TEMPORARY_NOT_FOUND_WITH_PUBLIC_ID, email);
        return  userTemporary;
    }

    @Override
    public UserTemporary createTempUser(User user) {
        UserTemporary userTemporary = new UserTemporary();
        userTemporary.setUser(user);
        return userTempRepository.save(userTemporary);
    }

    @Override
    public UserTemporary save(UserTemporary userTemporary) {
        return userTempRepository.save(userTemporary);
    }

    @Override
    public Collection<UserTemporary> findAllByUpdateDateTimeBefore(ZonedDateTime updateDateTime) {
        return userTempRepository.findAllByUpdateDateTimeBefore(updateDateTime);
    }
}
