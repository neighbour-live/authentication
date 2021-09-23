package com.bot.middleware.resources.services;

import com.bot.middleware.exceptions.type.ResourceAlreadyExistsException;
import com.bot.middleware.persistence.domain.ReportUser;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.request.AddReportUserRequest;

public interface ReportUserService {
    ReportUser addReportForUser(AddReportUserRequest addReportUserRequest, User reporterUser, User reportedUser) throws ResourceAlreadyExistsException;
}
