package com.app.middleware.resources.services;

import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.persistence.domain.Conversation;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserChat;
import com.app.middleware.persistence.request.AddChatRequest;
import com.app.middleware.persistence.response.PageableResponseEntity;

public interface ChatService {
    UserChat getChatByPublicId(String chatPublicId, User user) throws ResourceNotFoundException;

    PageableResponseEntity<Object> getChatPage(Integer pageNo, Integer pageSize, User user, Conversation conversation);

    UserChat createChat(AddChatRequest addChatRequest, User user) throws Exception;
}
