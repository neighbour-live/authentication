package com.app.middleware.persistence.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private String publicId;
    private String stripeId;
    private String connectId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String userName;
    private String fbId;
    private String ggId;
    private String accessToken;
    private String refreshToken;
    private String dob;

    private String imageUrl;
    private String provider;
    private String providerId;

    private String addressLine;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private String nationality;
    private String ethnicity;
    private String ip;
    private Double lat;
    private Double lng;
    private String apartmentAddress;

    private boolean cardVerified;
    private boolean bankVerified;
    private boolean emailVerified;
    private boolean phoneVerified;
    private boolean identificationVerified;

    private String createDateTime;
    private String updatedDateTime;

}
