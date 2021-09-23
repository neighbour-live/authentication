package com.bot.middleware.persistence.mapper;

import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.dto.ChatRecipientDTO;
import com.bot.middleware.persistence.dto.UserDTO;
import com.bot.middleware.persistence.dto.UserMinimalDetailsDTO;
import com.bot.middleware.utility.id.PublicIdGenerator;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserMapper {

    public static UserDTO createUserDTOLazy(User user) {
        UserDTO userDTO = UserDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(user.getPublicId()))
                .connectId(user.getConnectId())
                .stripeId(user.getStripeId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .accessToken(user.getAccessToken())
                .refreshToken(user.getRefreshToken())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .imageUrl(user.getImageUrl())
                .emailVerified(user.getEmailVerified())
                .phoneVerified(user.getPhoneVerified())
                .cardVerified(user.getCardVerified())
                .sterlingBackgroundVerified(user.getSterlingBackgroundVerified())
                .provider(user.getProvider().toString())
                .providerId(user.getProviderId())
                .dob((user.getDob() == null) ? "": user.getDob())
                .lat((user.getLat() == 0.0) ? 0.0f: user.getLat())
                .lng((user.getLng() == 0.0) ? 0.0f: user.getLng())
                .addressLine((user.getAddressLine() == null) ? "": user.getAddressLine())
                .apartmentAddress((user.getApartmentAddress() == null) ? "": user.getApartmentAddress())
                .postalCode((user.getPostalCode() == null) ? "": user.getPostalCode())
                .city((user.getCity() == null) ? "": user.getCity())
                .state((user.getState() == null) ? "": user.getState())
                .country((user.getCountry() == null) ? "": user.getCountry())
                .tagLine((user.getTagLine() == null) ? "": user.getTagLine())
                .bio((user.getBio() == null) ? "": user.getBio())
                .taskerRating((user.getTaskerRatingAvg() == null) ? 0.0f: user.getTaskerRatingAvg().floatValue())
                .posterRating((user.getPosterRatingAvg() == null) ? 0.0f: user.getPosterRatingAvg().floatValue())
                .tasksCompletedAsPoster((user.getTasksCompletedAsPoster() == null) ? 0 : user.getTasksCompletedAsPoster())
                .tasksCompletedAsTasker((user.getTasksCompletedAsTasker() == null) ? 0 : user.getTasksCompletedAsTasker())
                .createDateTime(user.getCreateDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .updatedDateTime(user.getUpdateDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        return userDTO;
    }

    public static List<UserDTO> createUserDTOListLazy(Collection<User> users) {
        List<UserDTO> userDTOList = new ArrayList<>();
        users.forEach(user -> userDTOList.add(createUserDTOLazy(user)));
        return userDTOList;
    }

    public static UserMinimalDetailsDTO createUserMinimalDetailsDTOLazy(User user) {
        UserMinimalDetailsDTO userMinimalDetailsDTO = UserMinimalDetailsDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(user.getPublicId()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .imageUrl(user.getImageUrl())
                .tagLine((user.getTagLine() == null) ? "": user.getTagLine())
                .bio((user.getBio() == null) ? "": user.getBio() )
                .posterRatingAverage((user.getPosterRatingAvg() == null) ? 0.0 : user.getPosterRatingAvg())
                .taskerRatingAverage((user.getTaskerRatingAvg() == null) ? 0.0 : user.getTaskerRatingAvg())
                .tasksCompletedAsPoster((user.getTasksCompletedAsPoster() == null) ? 0 : user.getTasksCompletedAsPoster())
                .tasksCompletedAsTasker((user.getTasksCompletedAsTasker() == null) ? 0 : user.getTasksCompletedAsTasker())
                .build();

        return userMinimalDetailsDTO;
    }

    public static List<UserMinimalDetailsDTO> createUserMinimalDetailsDTOListLazy(Collection<User> users) {
        List<UserMinimalDetailsDTO> userMinimalDetailsDTOS = new ArrayList<>();
        users.forEach(user -> userMinimalDetailsDTOS.add(createUserMinimalDetailsDTOLazy(user)));
        return userMinimalDetailsDTOS;
    }

    public static ChatRecipientDTO createChatRecipientDTOLazy(User user) {
        ChatRecipientDTO chatRecipientDTO = ChatRecipientDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(user.getPublicId()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .imageUrl(user.getImageUrl())
                .build();

        return chatRecipientDTO;
    }

    public static List<ChatRecipientDTO> createChatRecipientDTOListLazy(Collection<User> users) {
        List<ChatRecipientDTO> chatRecipientDTOS = new ArrayList<>();
        users.forEach(user -> chatRecipientDTOS.add(createChatRecipientDTOLazy(user)));
        return chatRecipientDTOS;
    }
}
