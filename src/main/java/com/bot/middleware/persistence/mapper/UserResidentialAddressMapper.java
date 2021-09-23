package com.bot.middleware.persistence.mapper;

import com.bot.middleware.persistence.domain.UserAddress;
import com.bot.middleware.persistence.dto.UserResidentialAddressDTO;
import com.bot.middleware.utility.id.PublicIdGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserResidentialAddressMapper {

    public static UserResidentialAddressDTO createUserResidentialAddressDTOLazy(UserAddress userAddress) {
        UserResidentialAddressDTO userResidentialAddressDTO = UserResidentialAddressDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(userAddress.getPublicId()))
                .addressType(userAddress.getAddressType())
                .addressLine(userAddress.getAddressLine())
                .apartmentAddress(userAddress.getApartmentAddress())
                .lat(userAddress.getLat())
                .lng(userAddress.getLng())
                .addressLine(userAddress.getAddressLine())
                .apartmentAddress(userAddress.getApartmentAddress())
                .country(userAddress.getCountry())
                .city(userAddress.getCity())
                .state(userAddress.getState())
                .postalCode(userAddress.getPostalCode())
                .userPublicId(PublicIdGenerator.encodedPublicId(userAddress.getUser().getPublicId()))
                .build();

        return userResidentialAddressDTO;
    }

    public static List<UserResidentialAddressDTO> createUserResidentialAddressDTOListLazy(Collection<UserAddress> userAddresses) {
        List<UserResidentialAddressDTO> userResidentialAddressDTOS = new ArrayList<>();
        userAddresses.forEach(userAddress -> userResidentialAddressDTOS.add(createUserResidentialAddressDTOLazy(userAddress)));
        return userResidentialAddressDTOS;
    }
}
