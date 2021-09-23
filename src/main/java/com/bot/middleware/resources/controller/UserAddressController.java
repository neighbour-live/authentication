package com.bot.middleware.resources.controller;

import com.bot.middleware.exceptions.ExceptionUtil;
import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.bot.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserAddress;
import com.bot.middleware.persistence.dto.StatusMessageDTO;
import com.bot.middleware.persistence.mapper.UserResidentialAddressMapper;
import com.bot.middleware.persistence.repository.UserRepository;
import com.bot.middleware.persistence.request.AddUserAddressRequest;
import com.bot.middleware.persistence.response.GenericResponseEntity;
import com.bot.middleware.resources.services.UserAddressService;
import com.bot.middleware.security.UserPrincipal;
import com.bot.middleware.utility.AuthConstants;
import com.bot.middleware.utility.StatusCode;
import com.bot.middleware.utility.id.PublicIdGenerator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user-address")
public class UserAddressController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAddressService userAddressService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to add user address.")
    public ResponseEntity<?> addUserAddress(@Valid @RequestBody AddUserAddressRequest addUserAddressRequest) throws Exception {
        try {
            User user = isCurrentUser(addUserAddressRequest.getUserPublicId());
            UserAddress userAddress = userAddressService.createUserAddress(addUserAddressRequest, user);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserResidentialAddressMapper.createUserResidentialAddressDTOLazy(userAddress), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PutMapping("/{userAddressPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to edit user address.")
    public ResponseEntity<?> editUserAddress(@Valid @RequestBody AddUserAddressRequest addUserAddressRequest, @PathVariable String userAddressPublicId) throws Exception {
        try {
            User user = isCurrentUser(userAddressPublicId);
            userAddressService.editUserAddress(addUserAddressRequest, userAddressPublicId);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("User Address" + AuthConstants.EDITED_SUCCESSFULLY)
                    .status(0)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch all user addresses.")
    public ResponseEntity<?> getAllUserAddresses(@RequestParam("userPublicId") String userPublicId) throws Exception {
        try {
            isCurrentUser(userPublicId);
            List<UserAddress> userAddresses = userAddressService.getAllUserAddresses(userPublicId);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserResidentialAddressMapper.createUserResidentialAddressDTOListLazy(userAddresses), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/{userAddressPublicId}/user/{userPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch specific user address.")
    public ResponseEntity<?> getByUserAddressId(@PathVariable String userAddressPublicId, @PathVariable String userPublicId) throws Exception {
        try {
            isCurrentUser(userPublicId);
            UserAddress userAddress = userAddressService.getByUserAddressId(userAddressPublicId);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserResidentialAddressMapper.createUserResidentialAddressDTOLazy(userAddress), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PatchMapping("/{userAddressPublicId}/user/{userPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to delete user address.")
    public ResponseEntity<?> deleteUserAddress(@PathVariable String userAddressPublicId, @PathVariable String userPublicId) throws Exception {
        try {
            isCurrentUser(userPublicId);
            userAddressService.deleteUserAddress(userAddressPublicId, userPublicId);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("User Address" + AuthConstants.DELETED_SUCCESSFULLY)
                    .status(0)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    public User isCurrentUser(String userPublicId) throws ResourceNotFoundException, UnauthorizedException {
        User user = userRepository.findByPublicId(PublicIdGenerator.decodePublicId(userPublicId));
        if(user == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH_PUBLIC_ID, userPublicId);

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!userPrincipal.getEmail().equals(user.getEmail())) throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);

        return user;
    }
}
