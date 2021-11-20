package com.app.middleware.persistence.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditProfileRequest {

    @NotBlank
    private String userPublicId;

    private String firstName;

    private String lastName;

    private String gender;

    private String userName;

    @Email(message = "Bad email format.")
    private String email;

    @Length(max = 11, min = 11)
    @Pattern(regexp = "[0-9]{11}", message = "Phone number should be 11 digits long.")
    private String phoneNumber;

    private String imageUrl;
    private String password;

    @NotNull
    @Min(-90)
    @Max(90)
    private float lat;
    @NotNull
    @Min(-180)
    @Max(180)
    private float lng;

    private String postalCode;
    private String city;
    private String state;
    private String country;
    private String addressLine;
    private String apartmentAddress;

    private String nationality;
    private String ethnicity;

    @Length(max = 15, min = 7)
    @Pattern(regexp = "((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)", message = "i.p. should be in format abc.efg.uvw.xyz")
    private String ip;

    @Length(max = 10)
    private String dob;

    private Boolean updateUserName;
    private Boolean updatePassword;
    private Boolean updatePhoneNumber;
    private Boolean updateEmail;
    private Boolean updatePersonalDetails;
    private Boolean emailVerified;
    private  Boolean updateAddressDetails;

}
