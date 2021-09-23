package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.Conversation;
import com.bot.middleware.persistence.domain.Task;
import com.bot.middleware.persistence.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ConversationRepository extends JpaRepository<Conversation,Long> {

    Conversation findByPublicId(Long decodePublicId);

    @Query(value = "SELECT c FROM Conversation c WHERE c.isDeleted = false AND c.isActive = true AND c.task = :task ")
    Conversation findByTask(Task task);

    @Query(value = "SELECT c FROM Conversation c WHERE c.isDeleted = false AND c.isActive = true AND ( c.poster = :user OR c.tasker = :user) ")
    List<Conversation> findAllByPosterOrTasker(User user);

    @Query( countQuery = "SELECT COUNT (c) FROM Conversation c " +
            "WHERE c.isDeleted = false AND c.isActive = true " +
            "AND (c.poster = :user OR c.tasker = :user) "
            , value = "SELECT c FROM Conversation c " +
            "WHERE c.isDeleted = false AND c.isActive = true " +
            "AND (c.poster = :user OR c.tasker = :user) "
    )
    Page<Conversation> getConversationPage(Pageable pageable, User user);
}
