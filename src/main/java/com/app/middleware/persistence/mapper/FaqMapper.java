package com.app.middleware.persistence.mapper;

import com.app.middleware.persistence.domain.Faq;
import com.app.middleware.persistence.dto.FaqDTO;
import com.app.middleware.utility.id.PublicIdGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FaqMapper {

    public static FaqDTO createFaqDTOLazy(Faq faq) {
        FaqDTO faqDTO = FaqDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(faq.getPublicId()))
                .description(faq.getDescription())
                .question(faq.getQuestion())
                .isActive(faq.getIsActive() == null ? false : true)
                .build();

        return faqDTO;
    }

    public static List<FaqDTO> createFaqDTOListLazy(Collection<Faq> faqs) {
        List<FaqDTO> faqDTOList = new ArrayList<>();
        faqs.forEach(faq -> faqDTOList.add(createFaqDTOLazy(faq)));
        return faqDTOList;
    }
}
