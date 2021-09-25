package com.app.middleware.persistence.dto;

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
