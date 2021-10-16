package com.app.middleware.persistence.repository;

import com.app.middleware.persistence.domain.Conversation;
import com.app.middleware.persistence.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation,Long>  {
    Conversation findByPublicId(Long decodePublicId);

    @Query(value = "SELECT c FROM Conversation c WHERE c.isDeleted = false AND c.isActive = true AND ( c.firstUser = :user OR c.secondUser = :user) ")
    List<Conversation> findAllByFirstOrSecondUser(User user);

    @Query( countQuery = "SELECT COUNT (c) FROM Conversation c " +
            "WHERE c.isDeleted = false AND c.isActive = true " +
            "AND (c.firstUser = :user OR c.secondUser = :user) "
            , value = "SELECT c FROM Conversation c " +
            "WHERE c.isDeleted = false AND c.isActive = true " +
            "AND (c.firstUser = :user OR c.secondUser = :user) "
    )
    Page<Conversation> getConversationPage(Pageable pageable, User user);
}
