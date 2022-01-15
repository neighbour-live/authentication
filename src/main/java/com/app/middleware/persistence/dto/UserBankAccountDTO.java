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
public class UserBankAccountDTO {

    private String publicId;
    private String stripeSourceId;
    private String bankName;
    private String accountHolderName;

    private String transitNumber;
    private String financialInstitutionNumber;
    private String accountNumber;
    // bank account = transitNumber-financialInstitutionNumber accountNumber
    private Boolean bankVerified;
    private Boolean isActive;
    private Boolean isDefault;
    private UserMinimalDetailsDTO user;
}
