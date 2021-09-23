package com.bot.middleware.resources.controller;

import com.bot.middleware.exceptions.ExceptionUtil;
import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.bot.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserBankAccount;
import com.bot.middleware.persistence.dto.StatusMessageDTO;
import com.bot.middleware.persistence.mapper.UserBankAccountMapper;
import com.bot.middleware.persistence.repository.UserRepository;
import com.bot.middleware.persistence.request.AddBankAccountRequest;
import com.bot.middleware.persistence.response.GenericResponseEntity;
import com.bot.middleware.resources.services.UserBankAccountService;
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
@RequestMapping("/user-bank")
public class UserBankAccountController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBankAccountService userBankAccountService;

    @SneakyThrows
    @PostMapping("/add-bank")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to add bank account.")
    public ResponseEntity<?> addBankAccount(@Valid @RequestBody AddBankAccountRequest addBankAccountRequest,
                                            @RequestParam("userPublicId") String userPublicId,
                                            @RequestParam("userConnectId") String userConnectId) throws Exception {
        try {
            isCurrentUser(userPublicId);
            UserBankAccount userBankAccount = userBankAccountService.addBankAccount(addBankAccountRequest, userPublicId, userConnectId);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserBankAccountMapper.createUserBankAccountDTOLazy(userBankAccount), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }

    }

    @GetMapping("/check-bank")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to check back account.")
    public ResponseEntity<?> checkBankAccount(@RequestParam("accountNumber") String accountNumber) throws Exception {
        try {
            Boolean response = userBankAccountService.checkBankAccountExist(accountNumber);
            return GenericResponseEntity.create(StatusCode.SUCCESS, response, HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @SneakyThrows
    @PostMapping("/verify-bank")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to verify bank account.")
    public ResponseEntity<?> verifyBankAccount(@RequestParam("userPublicId") String userPublicId,
                                               @RequestParam("userStripeId") String userStripeId,
                                               @RequestParam("stripeBankId") String stripeBankId) throws Exception {
        try {
            isCurrentUser(userPublicId);
            UserBankAccount userBankAccount = userBankAccountService.verifyBankAccount(userPublicId, userStripeId, stripeBankId);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserBankAccountMapper.createUserBankAccountDTOLazy(userBankAccount), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @SneakyThrows
    @GetMapping("/{userPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch all bank accounts of user.")
    public ResponseEntity<?> getAllBankAccounts(@PathVariable String userPublicId) throws Exception {
        try {
            User user = isCurrentUser(userPublicId);
            List<UserBankAccount> userBankAccountList = userBankAccountService.getAllBankAccounts(user);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserBankAccountMapper.createUserBankAccountDTOListLazy(userBankAccountList), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @SneakyThrows
    @DeleteMapping("/{bankAccountPublicId}/user/{userPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to delete bank account.")
    public ResponseEntity<?> deleteBankAccount(@PathVariable String bankAccountPublicId, @PathVariable String userPublicId) throws Exception {
        try {
            User user = isCurrentUser(userPublicId);
            userBankAccountService.deleteBankAccount(bankAccountPublicId, user);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("User Bank Account" + AuthConstants.DELETED_SUCCESSFULLY)
                    .status(0)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @SneakyThrows
    @PutMapping("/{bankAccountPublicId}/user/{userPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to edit bank account.")
    public ResponseEntity<?> editBankAccount(@Valid @RequestBody AddBankAccountRequest addBankAccountRequest,
                                               @PathVariable String bankAccountPublicId,
                                               @PathVariable String userPublicId) throws Exception {
        try {
            User user = isCurrentUser(userPublicId);
            userBankAccountService.editBankAccount(bankAccountPublicId, user, addBankAccountRequest);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("User Bank Account" + AuthConstants.EDITED_SUCCESSFULLY)
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
