package com.app.middleware.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BalanceTransactionDTO {

    private String balanceTransactionId;
    private Double paidAmount;
    private Double netAmount;
    private Double feeAmount;
    private BigDecimal exchangeRate;
    private String currency;
    private String status;
    private String feeType;
    private String description;
    private Long createdOn;
    private Long availableOn;
    private List<FeeDetailsDTO> feeList;

}
