package com.app.middleware.persistence.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.json.JSONObject;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.ws.rs.Encoded;

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
    @Email(message = "Bad email format.")
    private String email;

    @NotBlank
    private String password;

    @Length(max = 11, min = 11)
    @Pattern(regexp = "[0-9]{11}", message = "Phone number should be 11 digits long.")
    @NotBlank
    private String phoneNumber;

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

    @Length(max = 15, min = 7)
    @Pattern(regexp = "((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)", message = "i.p. should be in format abc.efg.uvw.xyz")
    @NotBlank
    private String ip;

    @Length(max = 10)
    @NotBlank
    private String dob;

    private float lat;

    private float lng;

    private String apartmentAddress;

    private String firebaseKey;
}
