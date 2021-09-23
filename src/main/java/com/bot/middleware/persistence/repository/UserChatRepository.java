package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserChat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserChatRepository extends JpaRepository<UserChat,Long> {
    UserChat findByPublicId(Long publicId);

    @Query( countQuery = "SELECT COUNT (uc) FROM UserChat uc " +
            "WHERE uc.isDeleted = false AND uc.isActive = true AND uc.task.publicId = :taskPublicId "
            , value = "SELECT uc FROM UserChat uc " +
            "WHERE uc.isDeleted = false AND uc.isActive = true AND uc.task.publicId = :taskPublicId "
    )
    Page<UserChat> getChatsPage(Pageable pageable, Long taskPublicId);
}
