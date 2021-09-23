package com.bot.middleware.resources.services;

import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserCertification;
import com.bot.middleware.persistence.request.AddUserCertification;

import java.util.List;

public interface CertificationsService {
    boolean deleteUserCertification(String userCertificationPublicId, String userPublicId) throws ResourceNotFoundException, UnauthorizedException;

    UserCertification createUserCertification(AddUserCertification addUserCertification, User user);

    List<UserCertification> getAllUserCertifications(String userPublicId);

    UserCertification editUserCertification(AddUserCertification addUserCertification, String userCertificationPublicId) throws ResourceNotFoundException;

    UserCertification getByUserCertificationId(String userCertificationPublicId) throws ResourceNotFoundException;
}
