package com.app.middleware.persistence.repository;

import com.app.middleware.persistence.domain.UserChat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserChatRepository extends JpaRepository<UserChat,Long> {
    UserChat findByPublicId(Long publicId);

    @Query( countQuery = "SELECT COUNT (uc) FROM UserChat uc " +
            "WHERE uc.isDeleted = false AND uc.isActive = true AND uc.conversation.publicId = :conversationPublicId "
            , value = "SELECT uc FROM UserChat uc " +
            "WHERE uc.isDeleted = false AND uc.isActive = true AND uc.conversation.publicId = :conversationPublicId "
    )
    Page<UserChat> getChatsPage(Pageable pageable, Long conversationPublicId);
}
