package com.app.middleware.persistence.response;
import lombok.*;


@Setter
@Getter
@NoArgsConstructor
public class PushNotificationResponse {
    private int status;
    private String message;

    public PushNotificationResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
