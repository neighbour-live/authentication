package com.bot.middleware.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConversationDTO {
    private String publicId;
    private String title;
    private String description;
    private String taskPublicId;
    private String taskCategory;
    private String createDateTime;
    private ChatDTO lastMessage;
    private ChatRecipientDTO recipient;
}
