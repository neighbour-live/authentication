package com.app.middleware.persistence.repository;

import com.app.middleware.persistence.domain.Award;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AwardRepository extends JpaRepository<Award,Long> {

    Award findByPublicId(Long decodePublicId);
}
