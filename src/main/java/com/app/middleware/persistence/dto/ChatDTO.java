package com.app.middleware.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatDTO {
    private String publicId;
    private String message;
    private String messageAttributes;
    private String senderName;
    private String senderPublicId;
    private String receiverName;
    private String receiverPublicId;
    private String createDateTime;
    private boolean isSent;
}
