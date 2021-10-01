package com.app.middleware.persistence.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.annotation.Nullable;
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

    @Email(message = "Bad email format.")
    private String email;

    @Length(max = 11, min = 11)
    @Pattern(regexp = "[0-9]{11}", message = "Phone number should be 11 digits long.")
    private String phoneNumber;

    private String imageUrl;
    private String password;
    private String bio;
    private String tagLine;

    @Min(-90)
    @Max(90)
    private Double lat;
    @Min(-180)
    @Max(180)
    private Double lng;

    private String postalCode;
    private String city;
    private String state;
    private String country;
    private String addressLine;
    private String apartmentAddress;

    @Length(max = 15, min = 7)
    @Pattern(regexp = "((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)", message = "i.p. should be in format abc.efg.uvw.xyz")
    private String ip;

    @Length(max = 10)
    private String dob;

    @Max(value = 1, message = "updatePassword can be true(1) or false(0)")
    @Min(value = 0, message = "updatePassword can be true(1) or false(0)")
    @NotNull
    private int updatePassword;

    @Max(value = 1, message = "updatePhoneNumber can be true(1) or false(0)")
    @Min(value = 0, message = "updatePhoneNumber can be true(1) or false(0)")
    @NotNull
    private int updatePhoneNumber;

    @Max(value = 1, message = "updateEmail can be true(1) or false(0)")
    @Min(value = 0, message = "updateEmail can be true(1) or false(0)")
    @NotNull
    private int updateEmail;

    @Max(value = 1, message = "updatePersonalDetails can be true(1) or false(0)")
    @Min(value = 0, message = "updatePersonalDetails can be true(1) or false(0)")
    @NotNull
    private int updatePersonalDetails;

    @Max(value = 1, message = "updateAddressDetails can be true(1) or false(0)")
    @Min(value = 0, message = "updateAddressDetails can be true(1) or false(0)")
    @NotNull
    private int updateAddressDetails;

}
