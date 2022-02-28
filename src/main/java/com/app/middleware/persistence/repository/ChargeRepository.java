package com.app.middleware.persistence.repository;

import com.app.middleware.persistence.domain.Charge;
import com.app.middleware.persistence.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChargeRepository extends JpaRepository<Charge, Long> {

    Charge findByPublicId(Long decodePublicId);

    @Query("SELECT c from Charge c LEFT JOIN FETCH c.creator cc LEFT JOIN FETCH c.payer cp where c.isDeleted = false " +
            "AND (cc = :user OR cp = :user)")
    List<Charge> findByPayerOrCreatorAndIsDeletedFalse(User user);
}
