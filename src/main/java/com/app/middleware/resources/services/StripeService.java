package com.app.middleware.resources.services;

import com.app.middleware.exceptions.type.UnauthorizedException;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserBankAccount;
import com.app.middleware.persistence.domain.UserPaymentCard;
import com.app.middleware.persistence.domain.UserTransactions;
import com.app.middleware.persistence.request.AddBankAccountRequest;
import com.app.middleware.persistence.request.AddPaymentCardRequest;
import com.app.middleware.persistence.request.CreateStripeConnectRequest;
import com.app.middleware.persistence.request.RedeemAmount;
import com.stripe.exception.StripeException;
import com.stripe.model.*;

import java.util.Map;

public interface StripeService {

    Customer createStripeCustomer(User user) throws StripeException;

    Customer retrieveStripeCustomer(String stripeCustomerId) throws StripeException;

    UserPaymentCard addPaymentCard(User user, AddPaymentCardRequest addPaymentCardRequest, String userConnectId) throws StripeException, UnauthorizedException;

    boolean deleteCard(User user, UserPaymentCard userPaymentCard) throws StripeException;

    UserBankAccount addBankAccount(User user, AddBankAccountRequest addBankAccountRequest, String connectId) throws StripeException, UnauthorizedException;

    UserBankAccount verifyBankAccount(User user, String connectId, String connectBankId) throws StripeException;

    boolean deleteBankAccount(User user, UserBankAccount userBankAccount) throws StripeException;

    Account createStripeCustomConnectAccount(CreateStripeConnectRequest stripeConnectRequest) throws StripeException;

    Account getConnectAccount(String connectId) throws StripeException;

    Account setDefaultCurrencyAccount(String userConnectId, String connectSourceId, boolean isCard, User user) throws StripeException;

    Map<String, Object> retrieveBalance(User user) throws StripeException;

    Account setPaymentFrequency(User user, String interval, int weeklyAnchor, int monthlyAnchor, int delayDays) throws StripeException;

    BalanceTransaction getBalanceTransactionDebit(String paymentId) throws StripeException;

    BalanceTransaction getBalanceTransactionCredit(String paymentId) throws StripeException;

    Card editCard(User user, UserPaymentCard userPaymentCard, AddPaymentCardRequest addPaymentCardRequest) throws StripeException;

    BankAccount editBankAccount(User user, UserBankAccount userBankAccount, AddBankAccountRequest addBankAccountRequest) throws StripeException;


//    UserTransactions chargeMoneyFromTaskPoster(Task task) throws StripeException;
//
//    UserTransactions transferMoneyFromPlatformToTasker(Task task) throws StripeException;

    Payout subtractMoneyAndSendItToUserPaymentOption(RedeemAmount redeemAmount, User user) throws StripeException;
}
