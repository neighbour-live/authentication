package com.bot.middleware.persistence.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddUserBidRequest {

    @NotBlank
    private String description;

    @NotBlank
    private String userPublicId;

    @NotBlank
    private String taskPublicId;

    @NotNull
    private int budget;
    private double otherCosts;
    private double hourlyRate;
    private double hours;
    private String otherCostsExplanation;
    private String timeUtilizationExplanation;
    private String imageUrl;

    private String ipAddress;
}
