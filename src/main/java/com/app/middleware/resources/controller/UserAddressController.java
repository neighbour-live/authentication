package com.app.middleware.resources.controller;

import com.app.middleware.exceptions.ExceptionUtil;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserAddress;
import com.app.middleware.persistence.dto.StatusMessageDTO;
import com.app.middleware.persistence.mapper.UserResidentialAddressMapper;
import com.app.middleware.persistence.request.AddUserAddressRequest;
import com.app.middleware.persistence.response.GenericResponseEntity;
import com.app.middleware.resources.services.AuthorizationService;
import com.app.middleware.resources.services.UserAddressService;
import com.app.middleware.utility.AuthConstants;
import com.app.middleware.utility.StatusCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user-address")
public class UserAddressController {

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private UserAddressService userAddressService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to add user address.")
    public ResponseEntity<?> addUserAddress(@Valid @RequestBody AddUserAddressRequest addUserAddressRequest) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(addUserAddressRequest.getUserPublicId());
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
            User user = authorizationService.isCurrentUser(addUserAddressRequest.getUserPublicId());
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
            authorizationService.isCurrentUser(userPublicId);
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
            authorizationService.isCurrentUser(userPublicId);
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
            authorizationService.isCurrentUser(userPublicId);
            userAddressService.deleteUserAddress(userAddressPublicId, userPublicId);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("User Address" + AuthConstants.DELETED_SUCCESSFULLY)
                    .status(0)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }
}
