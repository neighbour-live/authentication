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
public class UserWalletDTO {

    private String publicId;
    private String currency;
    private BigDecimal amount;
    private UserPaymentCardsDTO userPaymentCard;
    private UserBankAccountDTO userBankAccount;
    private String userPublicId;
}
