package com.app.middleware.persistence.repository;

import com.app.middleware.persistence.domain.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq,Long> {

    Faq findByPublicId(Long publicId);

    List<Faq> findByIsActiveTrue();
}
