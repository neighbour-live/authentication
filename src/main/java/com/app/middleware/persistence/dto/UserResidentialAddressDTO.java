package com.app.middleware.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResidentialAddressDTO {
    private String publicId;
    private boolean isDeleted = false;
    private String addressType;
    private String userPublicId;
    private float lat;
    private float lng;
    private String addressLine;
    private String apartmentAddress;
    private String country;
    private String state;
    private String city;
    private String postalCode;
}
