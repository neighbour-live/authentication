package com.bot.middleware.resources.services;

import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.persistence.domain.Award;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserAward;
import com.bot.middleware.persistence.request.AddAward;
import com.bot.middleware.persistence.request.AddUserAward;

import java.util.List;

public interface AwardsService {
    Award createAward(AddAward addAward);

    List<Award> getAllAwards();

    boolean deleteAward(String awardPublicId) throws ResourceNotFoundException;

    UserAward addUserAward(AddUserAward addUserAward, User user, Award award);

    UserAward getUserAward(String userAwardPublicId) throws ResourceNotFoundException;

    boolean deleteUserAward(String userAwardPublicId) throws ResourceNotFoundException;

    Award getByAwardId(String awardPublicId) throws ResourceNotFoundException;

    List<UserAward> getAllUserAwards(User user);
}
