package com.bot.middleware.persistence.mapper;

import com.bot.middleware.persistence.domain.Award;
import com.bot.middleware.persistence.domain.ReportUser;
import com.bot.middleware.persistence.dto.AwardDTO;
import com.bot.middleware.persistence.dto.ReportUserDTO;
import com.bot.middleware.utility.id.PublicIdGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReportUserMapper {

    public static ReportUserDTO createReportUserDTOLazy(ReportUser reportUser) {
        ReportUserDTO reportUserDTO = ReportUserDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(reportUser.getPublicId()))
                .issue(reportUser.getIssue())
                .reportedPublicId(PublicIdGenerator.encodedPublicId(reportUser.getReported().getPublicId()))
                .reporterPublicId(PublicIdGenerator.encodedPublicId(reportUser.getReporter().getPublicId()))
                .subject(reportUser.getSubject())
                .build();

        return reportUserDTO;
    }

    public static List<ReportUserDTO> createReportUserDTOListLazy(Collection<ReportUser> reportUsers) {
        List<ReportUserDTO> reportUserDTOs = new ArrayList<>();
        reportUsers.forEach(reportUser -> reportUserDTOs.add(createReportUserDTOLazy(reportUser)));
        return reportUserDTOs;
    }
}
