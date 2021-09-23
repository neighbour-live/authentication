package com.bot.middleware.persistence.dto;

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
public class UserCertificationsDTO {
    private String publicId;
    private String title;
    private String description;
    private String issuingInstitution;
    private String issuingDate;
    private String expiryDate;
    private String certificationURL;
    private String userPublicId;
}
