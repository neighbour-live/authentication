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
public class BalanceDTO {
    private Long available;
    private Long pending;

    private Long delayDays;
    private String interval;
    private Long monthlyAnchor;
    private String weeklyAnchor;
}
