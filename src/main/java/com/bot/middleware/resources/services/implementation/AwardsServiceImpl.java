package com.bot.middleware.resources.services.implementation;

import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.persistence.domain.Award;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserAward;
import com.bot.middleware.persistence.repository.AwardRepository;
import com.bot.middleware.persistence.repository.UserAwardsRepository;
import com.bot.middleware.persistence.request.AddAward;
import com.bot.middleware.persistence.request.AddUserAward;
import com.bot.middleware.persistence.type.AwardType;
import com.bot.middleware.resources.services.AwardsService;
import com.bot.middleware.utility.id.PublicIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AwardsServiceImpl implements AwardsService {

    @Autowired
    AwardRepository awardRepository;

    @Autowired
    UserAwardsRepository userAwardsRepository;

    @Override
    public Award createAward(AddAward addAward) {
        Award award = new Award();
        award.setAwardIcon(addAward.getAwardIconUrl());
        award.setAwardType(AwardType.valueOf(addAward.getAwardType()).toString());
        award.setDescription(addAward.getDescription());
        award.setTitle(addAward.getTitle());
        award.setPublicId(PublicIdGenerator.generatePublicId());
        return awardRepository.save(award);
    }

    @Override
    public List<Award> getAllAwards() {
        return awardRepository.findAll();
    }

    @Override
    public boolean deleteAward(String awardPublicId) throws ResourceNotFoundException {
        Award award = awardRepository.findByPublicId(PublicIdGenerator.decodePublicId(awardPublicId));
        if(award == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.AWARD_NOT_FOUND_WITH_PUBLIC_ID, awardPublicId);
        awardRepository.delete(award);
        return true;
    }

    @Override
    public UserAward addUserAward(AddUserAward addUserAward, User user, Award award) {
        UserAward userAward = new UserAward();
        userAward.setPublicId(PublicIdGenerator.generatePublicId());
        userAward.setUser(user);
        userAward.setAward(award);
        userAward.setProgress(addUserAward.getProgress());
        userAward.setIsActive(Boolean.valueOf(addUserAward.isActive()));
        userAward.setIsUnlocked(Boolean.valueOf(addUserAward.isUnlocked()));
        return userAwardsRepository.save(userAward);
    }

    @Override
    public UserAward getUserAward(String userAwardPublicId) throws ResourceNotFoundException {
        UserAward userAward = userAwardsRepository.findByPublicId(PublicIdGenerator.decodePublicId(userAwardPublicId));
        if(userAward == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.AWARD_NOT_FOUND_WITH_PUBLIC_ID, userAwardPublicId);
        return userAward;
    }

    @Override
    public boolean deleteUserAward(String userAwardPublicId) throws ResourceNotFoundException {
        UserAward userAward = userAwardsRepository.findByPublicId(PublicIdGenerator.decodePublicId(userAwardPublicId));
        if(userAward == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.AWARD_NOT_FOUND_WITH_PUBLIC_ID, userAwardPublicId);
        userAward.setIsUnlocked(false);
        userAward.setIsActive(false);
        userAward.setProgress(0);
        userAwardsRepository.save(userAward);
        return true;
    }

    @Override
    public Award getByAwardId(String awardPublicId) throws ResourceNotFoundException {
        Award award = awardRepository.findByPublicId(PublicIdGenerator.decodePublicId(awardPublicId));
        if(award == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.AWARD_NOT_FOUND_WITH_PUBLIC_ID, awardPublicId);
        return award;
    }

    @Override
    public List<UserAward> getAllUserAwards(User user) {
        List<UserAward> userAwards = userAwardsRepository.findAllByUser(user);
        return userAwards;
    }
}
