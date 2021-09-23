package com.bot.middleware.resources.controller;

import com.bot.middleware.exceptions.ExceptionUtil;
import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.bot.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserPaymentCard;
import com.bot.middleware.persistence.dto.StatusMessageDTO;
import com.bot.middleware.persistence.mapper.UserPaymentCardMapper;
import com.bot.middleware.persistence.repository.UserRepository;
import com.bot.middleware.persistence.request.AddPaymentCardRequest;
import com.bot.middleware.persistence.response.GenericResponseEntity;
import com.bot.middleware.resources.services.UserPaymentCardService;
import com.bot.middleware.security.UserPrincipal;
import com.bot.middleware.utility.AuthConstants;
import com.bot.middleware.utility.StatusCode;
import com.bot.middleware.utility.id.PublicIdGenerator;
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
        isCurrentUser(userPublicId);
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
            User user = isCurrentUser(userPublicId);
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
            User user = isCurrentUser(userPublicId);
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
            User user = isCurrentUser(userPublicId);
            userPaymentCardService.deleteUserPaymentCard(connectCardPublicId, user);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("User Payment Card" + AuthConstants.DELETED_SUCCESSFULLY)
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
