package com.app.middleware.persistence.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateStripeConnectRequest {

    @NotBlank
    private Long userPublicId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email(message = "Bad email format.")
    private String email;

    @Length(max = 11, min = 11)
    @Pattern(regexp = "[0-9]{11}", message = "Phone number should be 11 digits long.")
    @NotBlank
    private String phoneNumber;

    //change for stripe connect
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
    @Pattern(regexp = "[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|1[0-3])", message = "Date of birth should be in format YYYY-MM-DD.")
    @NotBlank
    private String dob;

    public CreateStripeConnectRequest() {}
}
