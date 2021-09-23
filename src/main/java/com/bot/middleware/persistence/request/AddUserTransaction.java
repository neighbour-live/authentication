package com.bot.middleware.persistence.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddUserTransaction {

    @NotBlank
    private String transactionType;

    @NotBlank
    private BigDecimal amount;

    @NotBlank
    private String paymentId;

    @NotBlank
    private String userPublicId;

    private String ipAddress;
}
