package com.bot.middleware.resources.services.implementation;

import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.persistence.domain.*;
import com.bot.middleware.persistence.dto.ChatDTO;
import com.bot.middleware.persistence.mapper.ChatMapper;
import com.bot.middleware.persistence.repository.UserChatRepository;
import com.bot.middleware.persistence.request.AddChatRequest;
import com.bot.middleware.persistence.request.UserNotificationRequest;
import com.bot.middleware.persistence.response.PageableResponseEntity;
import com.bot.middleware.persistence.type.BidStatus;
import com.bot.middleware.persistence.type.NotificationAction;
import com.bot.middleware.persistence.type.NotificationEnum;
import com.bot.middleware.resources.services.ChatService;
import com.bot.middleware.resources.services.NotificationService;
import com.bot.middleware.resources.services.TaskingService;
import com.bot.middleware.resources.services.UserService;
import com.bot.middleware.utility.StatusCode;
import com.bot.middleware.utility.id.PublicIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatServiceImpl implements ChatService {

    @Value("${bot.logoURL}")
    private String BOT_LOGO_URL;

    @Autowired
    private TaskingService taskingService;

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
    public PageableResponseEntity<Object> getChatPage(Integer pageNo, Integer pageSize, User user, Task task) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createDateTime").descending());
        Page<UserChat> chatPage = userChatRepository.getChatsPage(pageable, task.getPublicId());
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
        Task task = taskingService.getByTaskPublicId(addChatRequest.getTaskPublicId());
        if( task.getPoster().getPublicId().equals(user.getPublicId()) || task.getTasker().getPublicId().equals(user.getPublicId()) ){

            UserChat userChat = new UserChat();
            userChat.setPublicId(PublicIdGenerator.generatePublicId());
            userChat.setIsActive(true);
            userChat.setIsDeleted(false);
            userChat.setTask(task);
            userChat.setSender(user);
            userChat.setMessage(addChatRequest.getMessage());
            userChat.setMessageAttributes("{\n" +
                    "          \"read_status\": true,\n" +
                    "          \"direction\": \"incoming\"\n" +
                    "     }");
            userChat.setReceiver((task.getTasker().equals(user)) ? task.getPoster(): task.getTasker());

            //Send PUSH
            Map<String,String> actionsInfo =  new HashMap<>();
            actionsInfo.put("taskPublicId", PublicIdGenerator.encodedPublicId(task.getPublicId()));
            actionsInfo.put("chatPublicId", PublicIdGenerator.encodedPublicId(userChat.getPublicId()));
            actionsInfo.put("taskPublicId", PublicIdGenerator.encodedPublicId(task.getPublicId()));

            UserNotificationRequest userNotificationRequest = new UserNotificationRequest();
            userNotificationRequest.setTarget("INDIVIDUAL");
            userNotificationRequest.setImageUrl(BOT_LOGO_URL);
            userNotificationRequest.setActionsInfo(actionsInfo);
            userNotificationRequest.setNotificationType(NotificationEnum.SEND_CHAT.toString());
            userNotificationRequest.setTitle(user.getFirstName() + " " + user.getLastName());
            userNotificationRequest.setActions(NotificationAction.ACCEPTED_BID_PAGE.name());
            userNotificationRequest.setBody(addChatRequest.getMessage());
            userNotificationRequest.setFirebaseKey(userChat.getReceiver().getFirebaseKey());
            notificationService.postUserNotification(userNotificationRequest, userChat.getReceiver());
            return userChatRepository.save(userChat);

        } else {
            throw new Exception("You are un-authorized to send chat for this task's conversation");
        }
    }
}
