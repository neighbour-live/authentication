package com.app.middleware.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserTransactionsDTO {

    private String publicId;
    private String transactionType;
    private BigDecimal amount;
    private String paymentId;
    private String userPublicId;

    private String purpose;
    private String description;
    private String category;

    private String createDateTime;
    private String updatedDateTime;
}
