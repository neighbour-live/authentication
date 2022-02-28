package com.app.middleware.persistence.request;

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
public class AddBankAccountRequest {

    @Length(max = 6, min = 6)
    @Pattern(regexp = "[0-9]{6}", message = "Your transit number has six digits and is on the left-most side of your cheque, please enter valid transit number.")
    @NotBlank
    private String transitNumber;

    @Length(max = 3, min = 3)
    @Pattern(regexp = "[0-9]{3}", message = "Your financial institution number from your bank, the three-digit number in the center of your cheque, please enter valid financial institution number.")
    @NotBlank
    private String financialInstitutionNumber;

    @Length(max = 12, min = 12)
    @Pattern(regexp = "[0-9]{12}", message = "Your account number is a personal, 12-digit number shown on the right-most side of your cheque, please enter valid account number.")
    @NotBlank
    private String accountNumber;

    @Length(max = 100, min = 2, message = "Name must be at least 2 character long and at most 100 characters long")
    @NotBlank
    private String accountHolderName;

    @Length(max = 100, min = 2, message = "Name must be at least 2 character long and at most 100 characters long")
    @NotBlank
    private String bankName;

    @Max(value = 1, message = "Default can be true(1) or false(0)")
    @Min(value = 0, message = "Default can be true(1) or false(0)")
    @NotNull
    private int defaultUsage;

    private String ipAddress;
}
