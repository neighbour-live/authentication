package com.app.middleware.persistence.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserNotificationRequest {
    private String userPublicId;
    private String target;
    private String title;
    private String body;
    private String firebaseKey;
    private String imageUrl;
    private String notificationType;
    private String actions;
    private Map<String,String> actionsInfo;
}
