package com.bot.middleware.persistence.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddUserCertification {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String issuingInstitution;

    @NotBlank
    private String issuingDate;

    @NotBlank
    private String expiryDate;

    @NotBlank
    private String certificationURL;

    @NotBlank
    private String userPublicId;

    private String ipAddress;
}
