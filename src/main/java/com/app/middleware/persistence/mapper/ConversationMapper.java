package com.app.middleware.persistence.mapper;

import com.app.middleware.persistence.domain.Conversation;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.dto.ConversationDTO;
import com.app.middleware.utility.id.PublicIdGenerator;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConversationMapper {

    public static ConversationDTO createConversationDTOLazy(Conversation conversation, User user) {
        User recipient = user.getId().equals(conversation.getFirstUser().getId()) ? conversation.getFirstUser() : conversation.getSecondUser();
        ConversationDTO conversationDTO = ConversationDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(conversation.getPublicId()))
                .createDateTime(conversation.getCreateDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .recipient(UserMapper.createChatRecipientDTOLazy(recipient))
                .build();

        return conversationDTO;
    }

    public static List<ConversationDTO> createConversationDTOListLazy(List<Conversation> conversations, User user) {
        List<ConversationDTO> conversationDTOS = new ArrayList<>();
        conversations.forEach(conversation -> conversationDTOS.add(createConversationDTOLazy(conversation, user)));
        return conversationDTOS;
    }
}
