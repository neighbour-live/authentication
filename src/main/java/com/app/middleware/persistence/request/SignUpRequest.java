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
public class SignUpRequest {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String imageUrl;
    @NotBlank
    private String addressLine;
    @NotBlank
    private String postalCode;
    @NotBlank
    private String city;
    @NotBlank
    private String state;
    @NotBlank
    private String country;
    @NotBlank
    private String nationality;
    @NotBlank
    private String ethnicity;
    @NotBlank
    private String gender;

    @Length(max = 15, min = 7)
    @Pattern(regexp = "((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)", message = "i.p. should be in format 000.000.000.000")
    @NotBlank
    private String ip;
    @Length(max = 10)
    @NotBlank
    private String dob;

    @NotNull
    @Min(-90)
    @Max(90)
    private float lat;
    @NotNull
    @Min(-180)
    @Max(180)
    private float lng;


    @NotBlank(message = "User publicId is required")
    private String publicId;

    @NotBlank
    private String password;

    private String email;
    private String userName;
    private String provider;
    private String phoneNumber;
    private Boolean phoneVerified;
    private Boolean emailVerified;
    private String apartmentAddress;
    private String fbId;
    private String ggId;
    private String firebaseKey;
}
