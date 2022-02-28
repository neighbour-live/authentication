package com.app.middleware.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogsDTO {
    private long id;
    private String userPublicId;
    private String request;
    private String response;
    private String ipAddress;
    private String createDateTime;
}
