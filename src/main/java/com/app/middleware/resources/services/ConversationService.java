package com.app.middleware.resources.services;

import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.persistence.domain.Conversation;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.request.AddConversationRequest;
import com.app.middleware.persistence.response.PageableResponseEntity;

import java.util.List;

public interface ConversationService {
    Conversation createConversation(AddConversationRequest addConversationRequest, User first, User second);

    Conversation createConversation(AddConversationRequest addConversationRequest, User user);

    Conversation getConversation(String taskPublicId, User user) throws ResourceNotFoundException;

    PageableResponseEntity<Object> getConversationPage(Integer pageNo, Integer pageSize, User user);

    List<Conversation> getAllConversations(User user);

    Conversation deleteConversationByTask(Conversation conversation) throws ResourceNotFoundException;

    Conversation getConversationByPublicId(String conversationPublicId) throws ResourceNotFoundException;
}
