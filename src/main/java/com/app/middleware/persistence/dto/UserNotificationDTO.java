package com.app.middleware.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserNotificationDTO {

    private String publicId;
    private String title;
    private String body;
    private String imageUrl;
    private String actions;
    private Boolean isDeleted;
    private Boolean isRead;
    private Boolean isArchived;
    private String actionsInfo;
    private String notificationType;
    private String createDateTime;
}
