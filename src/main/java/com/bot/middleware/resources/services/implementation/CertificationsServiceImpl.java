package com.bot.middleware.resources.services.implementation;

import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.bot.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserCertification;
import com.bot.middleware.persistence.repository.UserCertificationsRepository;
import com.bot.middleware.persistence.request.AddUserCertification;
import com.bot.middleware.resources.services.CertificationsService;
import com.bot.middleware.utility.id.PublicIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CertificationsServiceImpl implements CertificationsService {
    @Autowired
    private UserCertificationsRepository userCertificationsRepository;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean deleteUserCertification(String userCertificationPublicId, String userPublicId) throws ResourceNotFoundException, UnauthorizedException {
        UserCertification userCertification = userCertificationsRepository.findByPublicId(PublicIdGenerator.decodePublicId(userCertificationPublicId));
        if(userCertification == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_CERTIFICATION_NOT_FOUND_PUBLIC_ID, userCertificationPublicId);
        if(!userCertification.getUser().getPublicId().equals(PublicIdGenerator.decodePublicId(userPublicId))) throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
        userCertificationsRepository.deleteUserCertification(PublicIdGenerator.decodePublicId(userCertificationPublicId));
        return true;
    }

    @Override
    public UserCertification createUserCertification(AddUserCertification addUserCertification, User user) {
        UserCertification userCertification = new UserCertification();
        userCertification.setPublicId(PublicIdGenerator.generatePublicId());
        userCertification.setUser(user);
        userCertification.setCertificationURL(addUserCertification.getCertificationURL());
        userCertification.setIssuingDate(addUserCertification.getIssuingDate());
        userCertification.setExpiryDate(addUserCertification.getIssuingDate());
        userCertification.setIssuingInstitution(addUserCertification.getIssuingInstitution());
        userCertification.setTitle(addUserCertification.getTitle());
        userCertification.setIsApproved(true);
        userCertification.setIsDeleted(false);
        userCertification.setDescription(addUserCertification.getDescription());
        return userCertificationsRepository.save(userCertification);
    }

    @Override
    public List<UserCertification> getAllUserCertifications(String userPublicId) {
        return userCertificationsRepository.findAllByUserAndIsDeletedIsFalse(PublicIdGenerator.decodePublicId(userPublicId));
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public UserCertification editUserCertification(AddUserCertification addUserCertification, String userCertificationPublicId) throws ResourceNotFoundException {
        UserCertification userCertification = userCertificationsRepository.findByPublicId(PublicIdGenerator.decodePublicId(userCertificationPublicId));
        if(userCertification == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_CERTIFICATION_NOT_FOUND_PUBLIC_ID, userCertificationPublicId);

        userCertification.setCertificationURL(addUserCertification.getCertificationURL());
        userCertification.setIssuingDate(addUserCertification.getIssuingDate());
        userCertification.setIssuingInstitution(addUserCertification.getIssuingInstitution());
        userCertification.setTitle(addUserCertification.getTitle());
        userCertification.setDescription(addUserCertification.getDescription());

        return userCertificationsRepository.save(userCertification);
    }

    @Override
    public UserCertification getByUserCertificationId(String userCertificationPublicId) throws ResourceNotFoundException {
        UserCertification userCertification = userCertificationsRepository.findByPublicId(PublicIdGenerator.decodePublicId(userCertificationPublicId));
        if(userCertification == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_CERTIFICATION_NOT_FOUND_PUBLIC_ID, userCertificationPublicId);

        return userCertification;
    }
}
