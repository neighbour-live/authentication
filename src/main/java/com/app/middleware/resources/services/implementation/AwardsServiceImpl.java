package com.app.middleware.resources.services.implementation;

import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.persistence.domain.Award;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserAward;
import com.app.middleware.persistence.repository.AwardRepository;
import com.app.middleware.persistence.repository.UserAwardsRepository;
import com.app.middleware.persistence.request.AddAward;
import com.app.middleware.persistence.request.AddUserAward;
import com.app.middleware.persistence.type.AwardType;
import com.app.middleware.resources.services.AwardsService;
import com.app.middleware.utility.id.PublicIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
        userAward.setProgress(100);
        userAward.setIsActive(true);
        userAward.setIsUnlocked(true);
        return userAwardsRepository.save(userAward);
    }

    @Override
    public UserAward getUserAward(String userAwardPublicId) throws ResourceNotFoundException {
        UserAward userAward = userAwardsRepository.findByPublicId(PublicIdGenerator.decodePublicId(userAwardPublicId));
        if(userAward == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.AWARD_NOT_FOUND_WITH_PUBLIC_ID, userAwardPublicId);
        return userAward;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean deleteUserAward(String userAwardPublicId) throws ResourceNotFoundException {
        UserAward userAward = userAwardsRepository.findByPublicId(PublicIdGenerator.decodePublicId(userAwardPublicId));
        if(userAward == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.AWARD_NOT_FOUND_WITH_PUBLIC_ID, userAwardPublicId);
        userAwardsRepository.delete(userAward);
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

    @Override
    public Award addAward(AddAward addAward, User user) {

        Award award = new Award();
        award.setAwardIcon(addAward.getAwardIconUrl());
        award.setAwardType(addAward.getAwardType());
        award.setDescription(addAward.getDescription());
        award.setPublicId(PublicIdGenerator.generatePublicId());
        award.setTitle(addAward.getTitle());
        return awardRepository.save(award);
    }
}
