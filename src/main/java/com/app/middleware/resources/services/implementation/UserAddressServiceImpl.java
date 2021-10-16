package com.app.middleware.resources.services.implementation;

import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.exceptions.type.UnauthorizedException;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserAddress;
import com.app.middleware.persistence.repository.UserRepository;
import com.app.middleware.persistence.repository.UserResidentialAddressRepository;
import com.app.middleware.persistence.request.AddUserAddressRequest;
import com.app.middleware.resources.services.UserAddressService;
import com.app.middleware.utility.id.PublicIdGenerator;
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
