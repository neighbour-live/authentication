package com.app.middleware.persistence.repository;

import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserPaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserPaymentCardsRepository extends JpaRepository<UserPaymentCard,Long> {
    List<UserPaymentCard> findAllByUser(User user);

    UserPaymentCard findByPublicId(Long userPaymentCardPublicId);

    @Query(value = "SELECT COUNT(upc) FROM UserPaymentCard upc WHERE upc.user = :user")
    int countByUser(User user);
}
