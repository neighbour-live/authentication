package com.app.middleware.persistence.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddChatRequest {
    private String message;
    private String conversationPublicId;
    private String userPublicId;
}
