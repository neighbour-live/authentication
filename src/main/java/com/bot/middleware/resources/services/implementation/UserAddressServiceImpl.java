package com.bot.middleware.resources.services.implementation;

import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.bot.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserAddress;
import com.bot.middleware.persistence.domain.UserCertification;
import com.bot.middleware.persistence.repository.UserRepository;
import com.bot.middleware.persistence.repository.UserResidentialAddressRepository;
import com.bot.middleware.persistence.request.AddUserAddressRequest;
import com.bot.middleware.persistence.type.AddressType;
import com.bot.middleware.resources.services.AuthService;
import com.bot.middleware.resources.services.UserAddressService;
import com.bot.middleware.resources.services.UserService;
import com.bot.middleware.utility.id.PublicIdGenerator;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserResidentialAddressRepository userResidentialAddressRepository;

    @Override
    public UserAddress createUserAddress(AddUserAddressRequest addUserAddressRequest, User user) {

        UserAddress userAddress = new UserAddress();
        userAddress.setPublicId(PublicIdGenerator.generatePublicId());
        userAddress.setUser(user);
        userAddress.setAddressLine(addUserAddressRequest.getAddressLine());
        userAddress.setApartmentAddress(addUserAddressRequest.getApartmentAddress());
        userAddress.setAddressType(addUserAddressRequest.getAddressType());
        userAddress.setIsDeleted(false);
        userAddress.setLat(addUserAddressRequest.getLat());
        userAddress.setLng(addUserAddressRequest.getLng());
        userAddress.setCountry(addUserAddressRequest.getCountry());
        userAddress.setState(addUserAddressRequest.getState());
        userAddress.setPostalCode(addUserAddressRequest.getPostalCode());
        userAddress.setCity(addUserAddressRequest.getCity());
        return userResidentialAddressRepository.save(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public UserAddress editUserAddress(AddUserAddressRequest addUserAddressRequest, String userAddressPublicId) throws ResourceNotFoundException {
        UserAddress userAddress = userResidentialAddressRepository.findByPublicId(PublicIdGenerator.decodePublicId(userAddressPublicId));
        if(userAddress == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_ADDRESS_NOT_FOUND_PUBLIC_ID, userAddressPublicId);

        userAddress.setAddressLine(addUserAddressRequest.getAddressLine());
        userAddress.setApartmentAddress(addUserAddressRequest.getApartmentAddress());
        userAddress.setAddressType(addUserAddressRequest.getAddressType());
        userAddress.setIsDeleted(false);
        userAddress.setLat(addUserAddressRequest.getLat());
        userAddress.setLng(addUserAddressRequest.getLng());
        userAddress.setCountry(addUserAddressRequest.getCountry());
        userAddress.setState(addUserAddressRequest.getState());
        userAddress.setPostalCode(addUserAddressRequest.getPostalCode());
        userAddress.setCity(addUserAddressRequest.getCity());
        return userResidentialAddressRepository.save(userAddress);
    }

    @Override
    public List<UserAddress> getAllUserAddresses(String userPublicId) {
        return userResidentialAddressRepository.findAllByUserAndIsDeletedIsFalse(PublicIdGenerator.decodePublicId(userPublicId));
    }

    @Override
    public UserAddress getByUserAddressId(String userAddressPublicId) throws ResourceNotFoundException {
        UserAddress userAddress = userResidentialAddressRepository.findByPublicId(PublicIdGenerator.decodePublicId(userAddressPublicId));
        if(userAddress == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_ADDRESS_NOT_FOUND_PUBLIC_ID, userAddressPublicId);
        return userAddress;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Boolean deleteUserAddress(String userAddressPublicId, String userPublicId) throws ResourceNotFoundException, UnauthorizedException {
        UserAddress userAddress = userResidentialAddressRepository.findByPublicId(PublicIdGenerator.decodePublicId(userAddressPublicId));
        if(userAddress == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_ADDRESS_NOT_FOUND_PUBLIC_ID, userAddressPublicId);
        if(!userAddress.getUser().getPublicId().equals(PublicIdGenerator.decodePublicId(userPublicId))) throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
        userResidentialAddressRepository.deleteUserAddress(PublicIdGenerator.decodePublicId(userAddressPublicId));
        return true;
    }

    @Override
    public UserAddress saveAddress(UserAddress userAddress) {
        return userResidentialAddressRepository.save(userAddress);
    }

    @Override
    public UserAddress getByUser(User user) {
        return userResidentialAddressRepository.getByUser(user);
    }
}
