package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.UserCertification;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCertificationsRepository extends JpaRepository<UserCertification,Long> {
    @Query(value = "SELECT uc FROM UserCertification uc WHERE uc.isDeleted = false AND uc.publicId = :publicId")
    UserCertification findByPublicId(Long publicId);

    @Query(value = "SELECT uc FROM UserCertification uc WHERE uc.isDeleted = false AND uc.user.publicId = :userPublicId")
    List<UserCertification> findAllByUserAndIsDeletedIsFalse(Long userPublicId);

    @Modifying
    @Query(value = "UPDATE UserCertification uc SET uc.isDeleted =true WHERE uc.publicId =:userCertificationPublicId")
    Integer deleteUserCertification(Long userCertificationPublicId);
}
