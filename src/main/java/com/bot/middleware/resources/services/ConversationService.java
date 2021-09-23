package com.bot.middleware.resources.services;

import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.persistence.domain.Conversation;
import com.bot.middleware.persistence.domain.Task;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserNotification;
import com.bot.middleware.persistence.request.AddConversationRequest;
import com.bot.middleware.persistence.response.PageableResponseEntity;

import java.util.List;

public interface ConversationService {
    Conversation createConversation(AddConversationRequest addConversationRequest, Task task, User tasker, User poster);

    Conversation createConversation(AddConversationRequest addConversationRequest, User user);

    Conversation getConversation(String taskPublicId, User user) throws ResourceNotFoundException;

    PageableResponseEntity<Object> getConversationPage(Integer pageNo, Integer pageSize, User user);

    List<Conversation> getAllConversations(User user);

    Conversation deleteConversationByTask(Task task) throws ResourceNotFoundException;
}
