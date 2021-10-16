package com.app.middleware.persistence.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddUserAddressRequest {

    @NotBlank
    private String addressLine;

    @NotBlank
    private String addressType;

    @NotBlank
    private String userPublicId;

    @NotNull
    @Min(-90)
    @Max(90)
    private float lat;
    @NotNull
    @Min(-180)
    @Max(180)
    private float lng;


    @NotBlank
    private String apartmentAddress;

    @NotBlank
    private String country;

    @NotBlank
    private String state;

    @NotBlank
    private String city;

    @NotBlank
    private String postalCode;

    private String ipAddress;

}
