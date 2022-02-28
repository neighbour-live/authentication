package com.app.middleware.persistence.repository;

import com.app.middleware.persistence.domain.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserWalletRepository extends JpaRepository<UserWallet,Long> {
    UserWallet findByPublicId(Long decodePublicId);

    @Query(value = "SELECT uw FROM UserWallet  uw WHERE uw.user.isDeleted = false AND uw.user.publicId =:userPublicId")
    UserWallet findByUserPublicId(Long userPublicId);
}
