package com.app.middleware.resources.controller;

import com.app.middleware.exception.ResourceNotFoundException;
import com.app.middleware.exceptions.ExceptionUtil;
import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.type.UnauthorizedException;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserBankAccount;
import com.app.middleware.persistence.dto.StatusMessageDTO;
import com.app.middleware.persistence.mapper.UserBankAccountMapper;
import com.app.middleware.persistence.repository.UserRepository;
import com.app.middleware.persistence.request.AddBankAccountRequest;
import com.app.middleware.persistence.response.GenericResponseEntity;
import com.app.middleware.resources.services.AuthorizationService;
import com.app.middleware.resources.services.UserBankAccountService;
import com.app.middleware.security.UserPrincipal;
import com.app.middleware.utility.AuthConstants;
import com.app.middleware.utility.StatusCode;
import com.app.middleware.utility.id.PublicIdGenerator;
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

    @Autowired
    private AuthorizationService authorizationService;

    @SneakyThrows
    @PostMapping("/add-bank")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to add bank account.")
    public ResponseEntity<?> addBankAccount(@Valid @RequestBody AddBankAccountRequest addBankAccountRequest,
                                            @RequestParam("userPublicId") String userPublicId,
                                            @RequestParam("userConnectId") String userConnectId) throws Exception {
        try {
            authorizationService.isCurrentUser(userPublicId);
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
            authorizationService.isCurrentUser(userPublicId);
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
            User user = authorizationService.isCurrentUser(userPublicId);
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
            User user = authorizationService.isCurrentUser(userPublicId);
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
            User user = authorizationService.isCurrentUser(userPublicId);
            userBankAccountService.editBankAccount(bankAccountPublicId, user, addBankAccountRequest);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("User Bank Account" + AuthConstants.EDITED_SUCCESSFULLY)
                    .status(0)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }
}
