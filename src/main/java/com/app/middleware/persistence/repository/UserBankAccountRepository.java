package com.app.middleware.persistence.repository;

import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserBankAccountRepository  extends JpaRepository<UserBankAccount,Long> {

    @Query(value = "SELECT uba FROM UserBankAccount uba WHERE uba.user =:user AND uba.isDeleted=false")
    UserBankAccount findByUser(User user);

    List<UserBankAccount> findAllByUser(User user);

    UserBankAccount findByPublicId(Long decodePublicId);

    @Query(value = "SELECT COUNT(uba) FROM UserBankAccount uba WHERE uba.user = :user")
    int countByUser(User user);
}
