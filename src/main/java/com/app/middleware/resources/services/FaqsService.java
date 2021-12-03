package com.app.middleware.resources.services;

import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.persistence.domain.Award;
import com.app.middleware.persistence.domain.Faq;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserAward;
import com.app.middleware.persistence.request.AddAward;
import com.app.middleware.persistence.request.AddFaq;
import com.app.middleware.persistence.request.AddUserAward;

import java.util.List;

public interface FaqsService {
    Faq createFaq(AddFaq faq);

    List<Faq> getAllFaqs();

    boolean deleteFaq(String faqPublicId) throws ResourceNotFoundException;

    Faq getByFaqId(String faqPublicId) throws ResourceNotFoundException;

    Faq editFaq(AddFaq addFaq, User user) throws ResourceNotFoundException;

    List<Faq> getAllFaqsForApp();
}
