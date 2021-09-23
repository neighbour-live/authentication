package com.bot.middleware.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserBidDTO {
    private String publicId;
    private UserMinimalDetailsDTO bidder;
    private TaskDTO task;
    private String taskPublicId;

    private Integer hours;
    private Integer hourlyRate;
    private BigInteger budget;
    private BigInteger otherCosts;
    private String description;
    private String otherCostsExplanation;
    private String timeUtilizationExplanation;
    private String imageUrl;
    private String status;
}
