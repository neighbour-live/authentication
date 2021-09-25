package com.app.middleware.persistence.mapper;

import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserChat;
import com.app.middleware.persistence.dto.ChatDTO;
import com.app.middleware.utility.id.PublicIdGenerator;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChatMapper {

    public static ChatDTO createChatDTOLazy(UserChat userChat) {

        User receiver = userChat.getReceiver();
        User sender = userChat.getSender();

        ChatDTO chatDTO = ChatDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(userChat.getPublicId()))
                .message(userChat.getMessage())
                .messageAttributes(userChat.getMessageAttributes())
                .receiverName(receiver.getFirstName() + " " + receiver.getLastName())
                .receiverPublicId(PublicIdGenerator.encodedPublicId(receiver.getPublicId()))
                .senderName(sender.getFirstName() + " " + sender.getLastName())
                .senderPublicId(PublicIdGenerator.encodedPublicId(sender.getPublicId()))
                .isSent(true)
                .createDateTime(userChat.getCreateDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        return chatDTO;
    }

    public static List<ChatDTO> createChatDTOListLazy(Collection<UserChat> userChats) {
        List<ChatDTO> chatDTOS = new ArrayList<>();
        userChats.forEach(userChat -> chatDTOS.add(createChatDTOLazy(userChat)));
        return chatDTOS;
    }
}
