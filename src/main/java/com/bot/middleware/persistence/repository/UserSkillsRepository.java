package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserSkillsRepository extends JpaRepository<UserSkill,Long> {
    UserSkill findByPublicId(Long decodePublicId);

    @Query(value = "SELECT us FROM UserSkill us WHERE us.isDeleted = false AND us.user.publicId = :userPublicId")
    List<UserSkill> findAllByUserAndIsDeletedIsFalse(Long userPublicId);

    @Modifying
    @Query(value = "UPDATE UserSkill us SET us.isDeleted =true WHERE us.publicId =:publicId")
    Integer deleteUserSkill(Long publicId);
}
