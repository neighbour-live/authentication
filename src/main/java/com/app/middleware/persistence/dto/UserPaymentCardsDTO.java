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
public class UserPaymentCardsDTO {
    private String publicId;
    private String stripeSourceId;
    private String connectSourceId;
    private String cardholderName;
    private String cardNumber;
    private String cardExpiryDate;
    private String cardType;
    private String cardBrand;
    private Boolean isActive;
    private Boolean isDefault;
    private String userPublicId;
}
