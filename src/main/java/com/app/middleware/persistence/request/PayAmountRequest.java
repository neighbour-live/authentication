package com.app.middleware.persistence.request;

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
public class PayAmountRequest {

    private float amount;
    private String payerPublicId;
    private String creatorPublicId;
    private String chargePublicId;
    private String stripeId;
    private String serviceType;
    private String reason;
    private String description;
    private String ipAddress;
    private Boolean payByWallet;
}
