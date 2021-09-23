package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.ReportUser;
import com.bot.middleware.persistence.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportUserRepository extends JpaRepository<ReportUser,Long> {
    ReportUser findByPublicId(Long decodePublicId);

    @Query(value = "SELECT r FROM ReportUser r " +
            "where r.reporter = :reporter " +
            "AND r.reported = :reportedUser " +
            "AND r.isResolved = :isResolved")
    ReportUser findByReporterAndReportedAndNotResolved(User reporter, User reportedUser, Boolean isResolved);
}
