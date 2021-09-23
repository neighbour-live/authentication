package com.bot.middleware.persistence.dto;

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
public class TaskTimelineDTO {
    private String publicId;
    private String statusChangeDatetime;
    private String status;
    private UserMinimalDetailsDTO tasker;
    private UserMinimalDetailsDTO poster;
}
