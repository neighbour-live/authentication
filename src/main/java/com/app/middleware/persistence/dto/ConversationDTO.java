package com.app.middleware.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConversationDTO {
    private String publicId;
    private String createDateTime;
    private ChatDTO lastMessage;
    private ChatRecipientDTO recipient;
}
