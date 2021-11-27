package com.app.middleware.persistence.dto;

import lombok.*;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailNotificationDto {

    private String to;
    private Map<String, String> placeHolders;
    private String template;


}
