package com.app.middleware.resources.services.implementation;

import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.persistence.domain.*;
import com.app.middleware.persistence.dto.ChatDTO;
import com.app.middleware.persistence.mapper.ChatMapper;
import com.app.middleware.persistence.repository.UserChatRepository;
import com.app.middleware.persistence.request.AddChatRequest;
import com.app.middleware.persistence.response.PageableResponseEntity;
import com.app.middleware.resources.services.ChatService;
import com.app.middleware.resources.services.ConversationService;
import com.app.middleware.resources.services.NotificationService;
import com.app.middleware.utility.StatusCode;
import com.app.middleware.utility.id.PublicIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Value("${logo-url}")
    private String LOGO_URL;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UserChatRepository userChatRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public UserChat getChatByPublicId(String chatPublicId, User user) throws ResourceNotFoundException {
        UserChat userChat = userChatRepository.findByPublicId(PublicIdGenerator.decodePublicId(chatPublicId));
        if(userChat == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.CHAT_NOT_FOUND_WITH_PUBLIC_ID, chatPublicId);
        return userChat;
    }

    @Override
    public PageableResponseEntity<Object> getChatPage(Integer pageNo, Integer pageSize, User user, Conversation conversation) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createDateTime").descending());
        Page<UserChat> chatPage = userChatRepository.getChatsPage(pageable, conversation.getPublicId());
        List<ChatDTO> chatDTOS = ChatMapper.createChatDTOListLazy(chatPage.getContent());
        PageableResponseEntity<Object> pageableResponseEntity = new PageableResponseEntity<Object>(
                StatusCode.SUCCESS,
                "Chats Page",
                chatDTOS,
                chatPage.getTotalElements(),
                chatPage.getSize(),
                chatPage.getTotalPages()
        );

        return pageableResponseEntity;
    }

    @Override
    public UserChat createChat(AddChatRequest addChatRequest, User user) throws Exception {
        Conversation conversation = conversationService.getConversationByPublicId(addChatRequest.getConversationPublicId());
        if( conversation.getFirstUser().getPublicId().equals(user.getPublicId()) || conversation.getSecondUser().getPublicId().equals(user.getPublicId()) ){

            UserChat userChat = new UserChat();
            userChat.setPublicId(PublicIdGenerator.generatePublicId());
            userChat.setIsActive(true);
            userChat.setIsDeleted(false);
            userChat.setConversation(conversation);
            userChat.setSender(user);
            userChat.setMessage(addChatRequest.getMessage());
            userChat.setMessageAttributes("{\n" +
                    "          \"read_status\": true,\n" +
                    "          \"direction\": \"incoming\"\n" +
                    "     }");
            userChat.setReceiver((conversation.getFirstUser().equals(user)) ? conversation.getFirstUser(): conversation.getSecondUser());

//            //Send PUSH
//            Map<String,String> actionsInfo =  new HashMap<>();
//            actionsInfo.put("conversationPublicId", PublicIdGenerator.encodedPublicId(conversation.getPublicId()));
//            actionsInfo.put("chatPublicId", PublicIdGenerator.encodedPublicId(userChat.getPublicId()));
//
//
//            UserNotificationRequest userNotificationRequest = new UserNotificationRequest();
//            userNotificationRequest.setTarget("INDIVIDUAL");
//            userNotificationRequest.setImageUrl(LOGO_URL);
//            userNotificationRequest.setActionsInfo(actionsInfo);
//            userNotificationRequest.setNotificationType(NotificationEnum.SEND_CHAT.toString());
//            userNotificationRequest.setTitle(user.getFirstName() + " " + user.getLastName());
//            userNotificationRequest.setActions(NotificationAction.ACCEPTED_BID_PAGE.name());
//            userNotificationRequest.setBody(addChatRequest.getMessage());
//            userNotificationRequest.setFirebaseKey(userChat.getReceiver().getFirebaseKey());
//            notificationService.postUserNotification(userNotificationRequest, userChat.getReceiver());
            return userChatRepository.save(userChat);

        } else {
            throw new Exception("You are un-authorized to send chat for this task's conversation");
        }
    }
}
