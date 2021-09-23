package com.bot.middleware.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDTO {
    private String publicId;
    private String title;
    private String description;
    private Boolean isAssigned;
    private Boolean userAlreadyPlacedBid;
    private Boolean feedbackAlreadyGiven;
    private Boolean isPending;
    private Boolean isCompleted;

    private TaskCategoriesDTO category;
    private UserMinimalDetailsDTO blockedBy;
    private UserMinimalDetailsDTO tasker;
    private UserMinimalDetailsDTO poster;

    private String taskRepeat;
    private String paymentType;
    private Float taskTime;
    private Integer hourlyRate;
    private Integer milestoneRate;
    private BigInteger budget;
    private String mediaFiles;
    private UserResidentialAddressDTO userAddress;
    private List<UserTransactionsDTO> userTransactions;

    private String startDateTime;
    private String endDateTime;
    private String createDateTime;
    private String updatedDateTime;

    private String status;
    private int totalBids;
    private Boolean isRemoteTask;
    private List<TaskTimelineDTO> taskTimeline;
    private Double additionalCharges;
}
