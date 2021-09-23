package com.bot.middleware.resources.services.implementation;

import com.bot.middleware.persistence.domain.SupportUser;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.repository.SupportUserRepository;
import com.bot.middleware.persistence.request.AddSupportUserRequest;
import com.bot.middleware.resources.services.ReportUserService;
import com.bot.middleware.resources.services.SupportUserService;
import com.bot.middleware.utility.id.PublicIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupportUserServiceImpl implements SupportUserService {

    @Autowired
    SupportUserRepository supportUserRepository;

    @Override
    public SupportUser addSupportForUser(AddSupportUserRequest addSupportUserRequest, User user) {
        SupportUser supportUser = new SupportUser();
        supportUser.setPublicId(PublicIdGenerator.generatePublicId());
        supportUser.setUser(user);
        supportUser.setDescription(addSupportUserRequest.getDescription());
        supportUser.setIsResolved(false);
        supportUser.setRelatedTo(addSupportUserRequest.getRelatedTo());
        return supportUserRepository.save(supportUser);
    }
}
