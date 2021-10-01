package com.app.middleware.resources.services.implementation;

import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserTemporary;
import com.app.middleware.persistence.repository.UserTempRepository;
import com.app.middleware.resources.services.UserTemporaryService;
import com.app.middleware.utility.id.PublicIdGenerator;
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
        UserTemporary userTemporary =  userTempRepository.findByPublicId(user.getPublicId());
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
        userTemporary.setPublicId(user.getPublicId());
        userTemporary.setPhoneVerified(false);
        userTemporary.setEmailVerified(false);
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

    @Override
    public UserTemporary findByPublicId(Long publicId) throws ResourceNotFoundException {
        UserTemporary userTemporary = userTempRepository.findByPublicId(publicId);
        if(userTemporary == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_TEMPORARY_NOT_FOUND_WITH_PUBLIC_ID);
        return userTemporary;
    }
}
