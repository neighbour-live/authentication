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
public class UserDTO {

    private String publicId;
    private String stripeId;
    private String connectId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String accessToken;
    private String refreshToken;
    private String tagLine;
    private String bio;
    private String dob;

    private String imageUrl;
    private String provider;
    private String providerId;

    private String addressLine;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private String ip;
    private float lat;
    private float lng;
    private String apartmentAddress;

    private boolean emailVerified;
    private boolean phoneVerified;
    private boolean cardVerified;
    private boolean sterlingBackgroundVerified;

    private float taskerRating;
    private float posterRating;

    private Integer tasksCompletedAsPoster;
    private Integer tasksCompletedAsTasker;

    private String createDateTime;
    private String updatedDateTime;

}
