package com.app.middleware.resources.services.implementation;

import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.persistence.domain.Faq;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.repository.FaqRepository;
import com.app.middleware.persistence.request.AddFaq;
import com.app.middleware.resources.services.FaqsService;
import com.app.middleware.utility.id.PublicIdGenerator;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaqsServiceImpl implements FaqsService {

    @Autowired
    private FaqRepository faqRepository;

    @Override
    public Faq createFaq(AddFaq addFaq) {
        Faq faq = new Faq();
        faq.setPublicId(PublicIdGenerator.generatePublicId());
        faq.setQuestion(addFaq.getQuestion());
        faq.setDescription(addFaq.getDescription());
        faq.setIsActive(true);

        return faqRepository.save(faq);
    }

    @Override
    public List<Faq> getAllFaqs() {
        return faqRepository.findAll();
    }

    @Override
    public boolean deleteFaq(String faqPublicId) throws ResourceNotFoundException {
        Faq faq = getByFaqId(faqPublicId);
        faqRepository.delete(faq);
        return true;
    }

    @Override
    public Faq getByFaqId(String faqPublicId) throws ResourceNotFoundException {
        Faq faq = faqRepository.findByPublicId(PublicIdGenerator.decodePublicId(faqPublicId));
        if(faq == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.FAQ_NOT_FOUND_WITH_FAQ_ID_FAQ);
        return faq;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Faq editFaq(AddFaq addFaq, User user) throws ResourceNotFoundException {
        Faq faq = getByFaqId(addFaq.getFaqPublicId());
        faq.setIsActive(addFaq.getIsActive() == null ? true: false);
        faq.setQuestion(addFaq.getQuestion());
        faq.setDescription(addFaq.getDescription());
        faq = faqRepository.save(faq);
        return faq;
    }

    @Override
    public List<Faq> getAllFaqsForApp() {
        return faqRepository.findByIsActiveTrue();
    }
}
