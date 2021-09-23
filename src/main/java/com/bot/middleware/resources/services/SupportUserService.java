package com.bot.middleware.resources.services;

import com.bot.middleware.persistence.domain.SupportUser;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.request.AddSupportUserRequest;

public interface SupportUserService {
    SupportUser addSupportForUser(AddSupportUserRequest addSupportUserRequest, User user);
}
