package com.bot.middleware.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
