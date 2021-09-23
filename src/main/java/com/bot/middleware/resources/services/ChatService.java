package com.bot.middleware.resources.services;

import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.persistence.domain.Task;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserChat;
import com.bot.middleware.persistence.request.AddChatRequest;
import com.bot.middleware.persistence.response.PageableResponseEntity;

public interface ChatService {
    UserChat getChatByPublicId(String chatPublicId, User user) throws ResourceNotFoundException;

    PageableResponseEntity<Object> getChatPage(Integer pageNo, Integer pageSize, User user, Task task);

    UserChat createChat(AddChatRequest addChatRequest, User user) throws Exception;
}
