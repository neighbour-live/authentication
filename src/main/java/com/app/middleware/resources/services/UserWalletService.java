package com.app.middleware.resources.services;

import com.app.middleware.exception.ResourceNotFoundException;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserTransactions;
import com.app.middleware.persistence.domain.UserWallet;
import com.app.middleware.persistence.dto.UserTransactionBreakdownDTO;
import com.app.middleware.persistence.request.RedeemAmount;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.Payout;

import java.util.List;
import java.util.Map;

public interface UserWalletService {

    Payout subtractMoneyAndSendItToUserPaymentOption(RedeemAmount redeemAmount, User user) throws StripeException;

    UserWallet getWalletDetails(User user);

    List<UserTransactions> getAllUserTransactions(User user);

    UserTransactionBreakdownDTO getTransactionByUserTransactionId(User user, String userTransactionPublicId) throws ResourceNotFoundException, StripeException;

    Account createConnectAccount(User user) throws StripeException;

    Account getConnectAccount(String accountId) throws StripeException;

    Account setDefaultCurrencyAccount(String userConnectId, String connectSourceId, boolean isCard, User user) throws StripeException;

    Map<String, Object> getBalance(User user) throws StripeException;

    Account setPaymentFrequency(User user, String interval, int weeklyAnchor, int monthlyAnchor, int delayDays) throws StripeException;
}
