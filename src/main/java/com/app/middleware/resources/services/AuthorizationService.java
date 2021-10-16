package com.app.middleware.resources.services;

import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.exceptions.type.UnauthorizedException;
import com.app.middleware.persistence.domain.User;

public interface AuthorizationService {
    User isCurrentUser(String userPublicId) throws ResourceNotFoundException, UnauthorizedException;
}
