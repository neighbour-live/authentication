package com.bot.middleware.resources.services.implementation;

import com.bot.middleware.exceptions.error.ResourceAlreadyExistErrorType;
import com.bot.middleware.exceptions.type.ResourceAlreadyExistsException;
import com.bot.middleware.persistence.domain.ReportUser;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.repository.ReportUserRepository;
import com.bot.middleware.persistence.request.AddReportUserRequest;
import com.bot.middleware.resources.services.ReportUserService;
import com.bot.middleware.utility.ObjectUtils;
import com.bot.middleware.utility.id.PublicIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportUserServiceImpl implements ReportUserService {

    @Autowired
    ReportUserRepository reportUserRepository;

    @Override
    public ReportUser addReportForUser(AddReportUserRequest addReportUserRequest, User reporterUser, User reportedUser) throws ResourceAlreadyExistsException {

        ReportUser reportUser = reportUserRepository.findByReporterAndReportedAndNotResolved(reporterUser, reportedUser, false);
        if(!ObjectUtils.isNull(reportUser)){
            throw new ResourceAlreadyExistsException(ResourceAlreadyExistErrorType.REPORT_TICKET_EXIST_WITH_PUBLIC_ID, PublicIdGenerator.encodedPublicId(reportUser.getPublicId()));
        }

        reportUser = new ReportUser();
        reportUser.setPublicId(PublicIdGenerator.generatePublicId());
        reportUser.setIsResolved(false);
        reportUser.setSubject(addReportUserRequest.getSubject());
        reportUser.setIssue(addReportUserRequest.getIssue());
        reportUser.setReporter(reporterUser);
        reportUser.setReported(reportedUser);
        return reportUserRepository.save(reportUser);
    }
}
