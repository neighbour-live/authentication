package com.app.middleware.resources.services.implementation;

import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.persistence.domain.Conversation;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.dto.ConversationDTO;
import com.app.middleware.persistence.mapper.ConversationMapper;
import com.app.middleware.persistence.repository.ConversationRepository;
import com.app.middleware.persistence.request.AddConversationRequest;
import com.app.middleware.persistence.response.PageableResponseEntity;
import com.app.middleware.resources.services.ConversationService;
import com.app.middleware.utility.StatusCode;
import com.app.middleware.utility.id.PublicIdGenerator;
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
    private ConversationRepository conversationRepository;

    @Override
    public Conversation createConversation(AddConversationRequest addConversationRequest, User first, User second) {
        Conversation conversation = new Conversation();
        conversation.setPublicId(PublicIdGenerator.generatePublicId());
        conversation.setFirstUser(first);
        conversation.setSecondUser(second);
        conversation.setIsActive(true);
        conversation.setIsDeleted(false);
        return conversationRepository.save(conversation);
    }

    @Override
    public Conversation createConversation(AddConversationRequest addConversationRequest, User user) {
        return null;
    }

    @Override
    public Conversation getConversation(String conversationPublicId, User user) throws ResourceNotFoundException {
        Conversation conversation = conversationRepository.findByPublicId(PublicIdGenerator.decodePublicId(conversationPublicId));
        if(conversation == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.CONVERSATION_NOT_FOUND_WITH_TASK_PUBLIC_ID, conversationPublicId);
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
        return conversationRepository.findAllByFirstOrSecondUser(user);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Conversation deleteConversationByTask(Conversation conversation) {
        conversation.setIsActive(false);
        conversation.setIsDeleted(true);
        return conversationRepository.save(conversation);
    }

    @Override
    public Conversation getConversationByPublicId(String conversationPublicId) throws ResourceNotFoundException {
        Conversation conversation = conversationRepository.findByPublicId(PublicIdGenerator.decodePublicId(conversationPublicId));
        if(conversation == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.CONVERSATION_NOT_FOUND_WITH_TASK_PUBLIC_ID, conversationPublicId);
        return conversation;
    }
}
