package com.bot.middleware.persistence.mapper;

import com.bot.middleware.persistence.domain.Conversation;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.dto.ConversationDTO;
import com.bot.middleware.utility.id.PublicIdGenerator;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConversationMapper {

    public static ConversationDTO createConversationDTOLazy(Conversation conversation, User user) {
        User recipient = user.getId().equals(conversation.getPoster().getId()) ? conversation.getTasker() : conversation.getPoster();
        ConversationDTO conversationDTO = ConversationDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(conversation.getPublicId()))
                .description(conversation.getDescription())
                .title(conversation.getTitle())
                .taskPublicId(PublicIdGenerator.encodedPublicId(conversation.getTask().getPublicId()))
                .taskCategory(conversation.getTask().getTaskCategory().getName())
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
