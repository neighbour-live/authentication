package com.bot.middleware.persistence.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddTask {

    @NotBlank
    private String categoryName;

    @NotBlank
    private String startDateTime;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String paymentType;

    @NotNull
    private int budget;

    @NotBlank
    private String posterPublicId;

    @NotBlank
    private String categoryPublicId;

    private String userAddressPublicId;

    private String mediaFiles;

    private float taskTime;

    private int hourlyRate;

    private int milestoneRate;

    private String taskRepeat;

    private String endDateTime;

    private String ipAddress;

    private Boolean isRemoteTask;
}
