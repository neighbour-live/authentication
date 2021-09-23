package com.bot.middleware.persistence.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddPaymentCardRequest {

    @Length(max = 16, min = 16)
    @Pattern(regexp = "[0-9]{16}", message = "Enter a valid 16 digits number")
    @NotBlank
    private String cardNumber;

    @Length(max = 2, min = 2)
    @Pattern(regexp = "[0-9]{2}", message = "Enter a valid 2 digit month")
    @NotBlank
    private String expMonth;

    @Length(max = 4, min = 4)
    @Pattern(regexp = "[0-9]{4}", message = "Enter a valid 4 digit year")
    @NotBlank
    private String expYear;

    @Length(max = 3, min = 3)
    @Pattern(regexp = "[0-9]{3}", message = "Enter a valid 3 digit number")
    @NotBlank
    private String cvc;

    @Max(value = 1, message = "Default can be true(1) or false(0)")
    @Min(value = 0, message = "Default can be true(1) or false(0)")
    @NotNull
    private int defaultUsage;

    private String ipAddress;
}
