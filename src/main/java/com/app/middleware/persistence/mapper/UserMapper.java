package com.app.middleware.persistence.mapper;

import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserTemporary;
import com.app.middleware.persistence.dto.*;
import com.app.middleware.utility.id.PublicIdGenerator;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserMapper {

    public static UserDTO createUserDTOLazy(User user) {
        UserDTO userDTO = UserDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(user.getPublicId()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .accessToken(user.getAccessToken())
                .refreshToken(user.getRefreshToken())
                .email(user.getEmail())
                .userName(user.getUserName())
                .fbId((user.getFbId() == null) ? "": user.getFbId())
                .ggId((user.getGgId() == null) ? "": user.getGgId())
                .phoneNumber(user.getPhoneNumber())
                .imageUrl(user.getImageUrl())
                .emailVerified(user.getEmailVerified() == null ? false: user.getEmailVerified())
                .phoneVerified(user.getPhoneVerified() == null ? false: user.getPhoneVerified())
                .identificationVerified(user.getIdentificationVerified() == null ? false: user.getIdentificationVerified())
                .provider(user.getProvider().toString())
                .providerId(user.getProviderId())
                .dob((user.getDob() == null) ? "": user.getDob())
                .lat((user.getLat() == Double.valueOf(0)) ? Double.valueOf(0): user.getLat())
                .lng((user.getLng() == Double.valueOf(0)) ? Double.valueOf(0): user.getLng())
                .addressLine((user.getAddressLine() == null) ? "": user.getAddressLine())
                .apartmentAddress((user.getApartmentAddress() == null) ? "": user.getApartmentAddress())
                .postalCode((user.getPostalCode() == null) ? "": user.getPostalCode())
                .city((user.getCity() == null) ? "": user.getCity())
                .state((user.getState() == null) ? "": user.getState())
                .country((user.getCountry() == null) ? "": user.getCountry())
                .nationality((user.getNationality() == null) ? "": user.getNationality())
                .ethnicity((user.getEthnicity() == null) ? "": user.getEthnicity())
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
                .userName(user.getUserName())
                .imageUrl(user.getImageUrl())
                .build();

        return userMinimalDetailsDTO;
    }

    public static List<UserMinimalDetailsDTO> createUserMinimalDetailsDTOListLazy(Collection<User> users) {
        List<UserMinimalDetailsDTO> userMinimalDetailsDTOS = new ArrayList<>();
        users.forEach(user -> userMinimalDetailsDTOS.add(createUserMinimalDetailsDTOLazy(user)));
        return userMinimalDetailsDTOS;
    }

    public static UserTemporaryDTO createUserTemporaryDTOLazy(UserTemporary userTemporary) {
        UserTemporaryDTO userTemporaryDTO = UserTemporaryDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(userTemporary.getPublicId()))
                .userName(userTemporary.getUserName() == null ? "" : userTemporary.getUserName() )
                .phoneNumber(userTemporary.getPhoneNumber() == null ? "" : userTemporary.getPhoneNumber())
                .phoneToken(userTemporary.getPhoneToken() == null ? "" : userTemporary.getPhoneToken())
                .email(userTemporary.getEmail() == null ? "" : userTemporary.getEmail())
                .emailToken(userTemporary.getEmailToken() == null ? "" :  userTemporary.getEmailToken())
                .emailVerified(userTemporary.getEmailVerified())
                .phoneVerified(userTemporary.getPhoneVerified())
                .build();

        return userTemporaryDTO;
    }

    public static List<UserIdentificationDTO> createUserIdentificationDTOListLazy(Collection<User> users) {
        List<UserIdentificationDTO> userIdentificationDTOS = new ArrayList<>();
        users.forEach(user -> userIdentificationDTOS.add(createUserIdentificationDTOLazy(user)));
        return userIdentificationDTOS;
    }

    public static UserIdentificationDTO createUserIdentificationDTOLazy(User user) {
        UserIdentificationDTO userIdentificationDTO = UserIdentificationDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(user.getPublicId()))
                .userName(user.getUserName() == null ? "" : user.getUserName() )
                .identificationVerified(user.getIdentificationVerified())
                .idDocBackUrl(user.getIdDocBackUrl() == null ? "" : user.getIdDocBackUrl())
                .idDocFrontUrl(user.getIdDocFrontUrl() == null ? "" : user.getIdDocFrontUrl())
                .build();

        return userIdentificationDTO;
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
