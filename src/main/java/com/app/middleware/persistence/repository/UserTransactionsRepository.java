package com.app.middleware.persistence.repository;

import com.app.middleware.persistence.domain.UserTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserTransactionsRepository extends JpaRepository<UserTransactions,Long> {
    UserTransactions findByPublicId(Long decodePublicId);

    @Query(value = "SELECT ut FROM UserTransactions ut WHERE ut.user.isDeleted = false AND ut.user.publicId =:userPublicId")
    List<UserTransactions> findAllByUserPublicId(Long userPublicId);
}
