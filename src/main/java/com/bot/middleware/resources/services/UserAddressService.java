package com.bot.middleware.resources.services;

import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserAddress;
import com.bot.middleware.persistence.domain.UserCertification;
import com.bot.middleware.persistence.request.AddUserAddressRequest;

import java.util.List;

public interface UserAddressService {

    UserAddress createUserAddress(AddUserAddressRequest addUserAddressRequest, User user);

    UserAddress editUserAddress(AddUserAddressRequest addUserAddressRequest, String userAddressPublicId) throws ResourceNotFoundException;

    List<UserAddress> getAllUserAddresses(String userPublicId);

    UserAddress getByUserAddressId(String userAddressPublicId) throws ResourceNotFoundException;

    Boolean deleteUserAddress(String userAddressPublicId, String userPublicId) throws ResourceNotFoundException, UnauthorizedException;

    UserAddress saveAddress(UserAddress userAddress);

    UserAddress getByUser(User user);
}
