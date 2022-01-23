package com.app.middleware.resources.controller;

import com.app.middleware.exceptions.ExceptionUtil;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserTransactions;
import com.app.middleware.persistence.domain.UserWallet;
import com.app.middleware.persistence.dto.UserTransactionBreakdownDTO;
import com.app.middleware.persistence.mapper.BalanceMapper;
import com.app.middleware.persistence.mapper.UserTransactionMapper;
import com.app.middleware.persistence.mapper.UserWalletMapper;
import com.app.middleware.persistence.repository.UserRepository;
import com.app.middleware.persistence.request.RedeemAmount;
import com.app.middleware.persistence.response.GenericResponseEntity;
import com.app.middleware.resources.services.AuthorizationService;
import com.app.middleware.resources.services.UserWalletService;
import com.app.middleware.utility.AuthConstants;
import com.app.middleware.utility.StatusCode;
import com.stripe.model.Account;
import com.stripe.model.Balance;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user-wallet")
public class UserWalletController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserWalletService userWalletService;

    @Autowired
    private AuthorizationService authorizationService;

    //Money going out of system into user bank account
    @PostMapping("/redeem")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to redeem amount from wallet.")
    public ResponseEntity<?> subtractMoneyAndSendItToUserPaymentOption(@Valid @RequestBody RedeemAmount redeemAmount) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(redeemAmount.getUserPublicId());
            return GenericResponseEntity.create(StatusCode.SUCCESS, userWalletService.subtractMoneyAndSendItToUserPaymentOption(redeemAmount, user), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch user wallet details.")
    public ResponseEntity<?> getUserWalletDetails(@RequestParam("userPublicId") String userPublicId) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);
            UserWallet userWallet = userWalletService.getWalletDetails(user);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserWalletMapper.createUserWalletDTOLazy(userWallet), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/transactions")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch all user transactions.")
    public ResponseEntity<?> getAllUserTransactions(@RequestParam("userPublicId") String userPublicId) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);
            List<UserTransactions> userTransactions = userWalletService.getAllUserTransactions(user);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserTransactionMapper.createUserTransactionDTOListLazy(userTransactions), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/transaction/{userTransactionPublicId}/user/{userPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch transaction detail.")
    public ResponseEntity<?> getByUserAddressId(@PathVariable String userTransactionPublicId, @PathVariable String userPublicId) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);
            UserTransactionBreakdownDTO userTransactionBreakdownDTO = userWalletService.getTransactionByUserTransactionId(user, userTransactionPublicId);
            return GenericResponseEntity.create(StatusCode.SUCCESS, userTransactionBreakdownDTO, HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    //Get Balance
    @GetMapping("/balance")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used fetch balance of user account.")
    public ResponseEntity<?> getBalance(@RequestParam("userPublicId") String userPublicId) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);
            Map<String, Object> response = userWalletService.getBalance(user);
            Balance balance = (Balance) response.get("balance");
            Account.PayoutSchedule payoutSchedule =  (Account.PayoutSchedule) response.get("paymentFrequency");
            return GenericResponseEntity.create(StatusCode.SUCCESS, BalanceMapper.createBalanceDTOLazy(balance, payoutSchedule), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PutMapping("/frequency")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch payment frequency.")
    public ResponseEntity<?> setPaymentFrequency(
            @NotNull @RequestParam("userPublicId") String userPublicId,
            @NotNull @RequestParam("interval") String interval,
            @RequestParam("weekDay") int weeklyAnchor,
            @RequestParam("monthDay") int monthlyAnchor,
            @RequestParam("delayDays") int delayDays) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);
            Account account = userWalletService.setPaymentFrequency(user, interval, weeklyAnchor, monthlyAnchor,delayDays);
            return GenericResponseEntity.create(null, StatusCode.SUCCESS, account.toJson(), null, HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }


    //Stripe connect pay-in and payout
    @GetMapping("/account/{accountId}/user/{userPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to connect pay-in and pay-out stripe account.")
    public ResponseEntity<?> getConnectAccount(@PathVariable String accountId, @PathVariable String userPublicId) throws Exception {
        try {
            authorizationService.isCurrentUser(userPublicId);
            Account account = userWalletService.getConnectAccount(accountId);
            return GenericResponseEntity.create(null, StatusCode.SUCCESS, account.toJson(), null, HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PostMapping("/account")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to create connect account.")
    public ResponseEntity<?> createConnectAccount(@RequestParam("userPublicId") String userPublicId) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);
            return GenericResponseEntity.create(StatusCode.SUCCESS, userWalletService.createConnectAccount(user), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PutMapping("/account")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to set default currency account.")
    public ResponseEntity<?> setDefaultCurrencyAccount(
            @RequestParam("userPublicId") String userPublicId,
            @RequestParam("userConnectId") String userConnectId,
            @RequestParam("connectSourceId") String connectSourceId,
            @RequestParam("isCard") boolean isCard) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);
            userWalletService.setDefaultCurrencyAccount(userConnectId, connectSourceId, isCard, user);
            return GenericResponseEntity.create(StatusCode.SUCCESS, "Default currency account " + AuthConstants.EDITED_SUCCESSFULLY, HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }
}
