package com.bot.middleware.persistence.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import javax.validation.constraints.NotBlank;

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

    private float lat;

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
