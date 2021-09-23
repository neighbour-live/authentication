package com.bot.middleware.resources.services.implementation;

import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.persistence.domain.Conversation;
import com.bot.middleware.persistence.domain.Task;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserNotification;
import com.bot.middleware.persistence.dto.ConversationDTO;
import com.bot.middleware.persistence.dto.UserNotificationDTO;
import com.bot.middleware.persistence.mapper.ConversationMapper;
import com.bot.middleware.persistence.mapper.UserNotificationMapper;
import com.bot.middleware.persistence.repository.ConversationRepository;
import com.bot.middleware.persistence.repository.UserRepository;
import com.bot.middleware.persistence.request.AddConversationRequest;
import com.bot.middleware.persistence.response.PageableResponseEntity;
import com.bot.middleware.resources.services.ConversationService;
import com.bot.middleware.resources.services.TaskingService;
import com.bot.middleware.utility.StatusCode;
import com.bot.middleware.utility.id.PublicIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private TaskingService taskingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Override
    public Conversation createConversation(AddConversationRequest addConversationRequest, Task task, User tasker, User poster) {
        Conversation conversation = new Conversation();
        conversation.setPublicId(PublicIdGenerator.generatePublicId());
        conversation.setPoster(poster);
        conversation.setTasker(tasker);
        conversation.setTask(task);
        conversation.setIsActive(true);
        conversation.setIsDeleted(false);
        conversation.setTitle(task.getTitle());
        conversation.setDescription(task.getDescription());
        return conversationRepository.save(conversation);
    }

    @Override
    public Conversation createConversation(AddConversationRequest addConversationRequest, User user) {
        return null;
    }

    @Override
    public Conversation getConversation(String taskPublicId, User user) throws ResourceNotFoundException {
        Task task = taskingService.getByTaskPublicId(taskPublicId);
        Conversation conversation = conversationRepository.findByTask(task);
        if(conversation == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.CONVERSATION_NOT_FOUND_WITH_TASK_PUBLIC_ID, PublicIdGenerator.encodedPublicId(task.getPublicId()));
        return conversation;
    }

    @Override
    public PageableResponseEntity<Object> getConversationPage(Integer pageNo, Integer pageSize, User user) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createDateTime").descending());
        Page<Conversation> conversationsPage = conversationRepository.getConversationPage(pageable, user);
        List<ConversationDTO> conversationDTOSList = ConversationMapper.createConversationDTOListLazy(conversationsPage.getContent(), user);
        PageableResponseEntity<Object> pageableResponseEntity = new PageableResponseEntity<Object>(
                StatusCode.SUCCESS,
                "Conversations Page",
                conversationDTOSList,
                conversationsPage.getTotalElements(),
                conversationsPage.getSize(),
                conversationsPage.getTotalPages()
        );

        return pageableResponseEntity;
    }

    @Override
    public List<Conversation> getAllConversations(User user) {
        return conversationRepository.findAllByPosterOrTasker(user);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Conversation deleteConversationByTask(Task task) throws ResourceNotFoundException {
        Conversation conversation = conversationRepository.findByTask(task);
        if(conversation == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.CONVERSATION_NOT_FOUND_WITH_TASK_PUBLIC_ID, PublicIdGenerator.encodedPublicId(task.getPublicId()));
        conversation.setIsActive(false);
        conversation.setIsDeleted(true);
        return conversationRepository.save(conversation);
    }
}
