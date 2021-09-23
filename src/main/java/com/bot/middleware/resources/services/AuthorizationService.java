package com.bot.middleware.resources.services;

import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.User;

public interface AuthorizationService {

    User isCurrentUser(String userPublicId) throws ResourceNotFoundException, UnauthorizedException;
}
