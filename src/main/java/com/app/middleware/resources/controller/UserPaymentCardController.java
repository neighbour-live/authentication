package com.app.middleware.resources.controller;

import com.app.middleware.exception.ResourceNotFoundException;
import com.app.middleware.exceptions.ExceptionUtil;
import com.app.middleware.exceptions.type.UnauthorizedException;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserPaymentCard;
import com.app.middleware.persistence.dto.StatusMessageDTO;
import com.app.middleware.persistence.mapper.UserPaymentCardMapper;
import com.app.middleware.persistence.repository.UserRepository;
import com.app.middleware.persistence.request.AddPaymentCardRequest;
import com.app.middleware.persistence.response.GenericResponseEntity;
import com.app.middleware.resources.services.AuthorizationService;
import com.app.middleware.resources.services.UserPaymentCardService;
import com.app.middleware.utility.AuthConstants;
import com.app.middleware.utility.StatusCode;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user-card")
public class UserPaymentCardController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPaymentCardService userPaymentCardService;

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("/check-card")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to check card.")
    public ResponseEntity<?> checkCard(@RequestParam("card") Long card) throws Exception {
        try {
            Boolean response = userPaymentCardService.checkCardExist(card);
            return GenericResponseEntity.create(StatusCode.SUCCESS, response, HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @SneakyThrows
    @PostMapping("/add-card")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to add payment card.")
    public ResponseEntity<?> addPaymentCard(@Valid @RequestBody AddPaymentCardRequest addPaymentCardRequest,
                                            @RequestParam("userPublicId") String userPublicId,
                                            @RequestParam("userConnectId") String userConnectId) throws Exception {
        authorizationService.isCurrentUser(userPublicId);
        UserPaymentCard userPaymentCard = userPaymentCardService.addPaymentCard(addPaymentCardRequest, userPublicId, userConnectId);
        return GenericResponseEntity.create(StatusCode.SUCCESS, UserPaymentCardMapper.createUserPaymentCardsDTOLazy(userPaymentCard), HttpStatus.OK);
    }

    @SneakyThrows
    @PutMapping("/{connectCardPublicId}/user/{userPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to edit user payment card.")
    public ResponseEntity<?> editUserPaymentCard(@Valid @RequestBody AddPaymentCardRequest addPaymentCardRequest,
                                                 @PathVariable String userPublicId,
                                                 @PathVariable String connectCardPublicId) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);
            userPaymentCardService.editPaymentCard(addPaymentCardRequest, user, connectCardPublicId);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("User Payment Card" + AuthConstants.EDITED_SUCCESSFULLY)
                    .status(0)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @SneakyThrows
    @GetMapping("/{userPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch all user payment cards.")
    public ResponseEntity<?> getAllUserPaymentCard(@PathVariable String userPublicId) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);
            List<UserPaymentCard> userPaymentCardList = userPaymentCardService.getAllUserPaymentCard(user);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserPaymentCardMapper.createUserPaymentCardsDTOListLazy(userPaymentCardList), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @SneakyThrows
    @DeleteMapping("/{connectCardPublicId}/user/{userPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to delete user payment card.")
    public ResponseEntity<?> deleteUserPaymentCard(@PathVariable String userPublicId, @PathVariable String connectCardPublicId) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);
            userPaymentCardService.deleteUserPaymentCard(connectCardPublicId, user);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("User Payment Card" + AuthConstants.DELETED_SUCCESSFULLY)
                    .status(0)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }
}
