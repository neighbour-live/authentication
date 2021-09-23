package com.bot.middleware.resources.services;

import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserTransactions;
import com.bot.middleware.persistence.domain.UserWallet;
import com.bot.middleware.persistence.dto.UserTransactionBreakdownDTO;
import com.bot.middleware.persistence.request.RedeemAmount;
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
