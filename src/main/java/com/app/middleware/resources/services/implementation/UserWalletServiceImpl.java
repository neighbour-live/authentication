package com.app.middleware.resources.services.implementation;

import com.app.middleware.exception.ResourceNotFoundException;
import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.type.UnauthorizedException;
import com.app.middleware.persistence.domain.Charge;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserTransactions;
import com.app.middleware.persistence.domain.UserWallet;
import com.app.middleware.persistence.dto.UserTransactionBreakdownDTO;
import com.app.middleware.persistence.mapper.BalanceTransactionMapper;
import com.app.middleware.persistence.mapper.UserTransactionMapper;
import com.app.middleware.persistence.repository.UserTransactionsRepository;
import com.app.middleware.persistence.repository.UserWalletRepository;
import com.app.middleware.persistence.request.CreateStripeConnectRequest;
import com.app.middleware.persistence.request.PayAmountRequest;
import com.app.middleware.persistence.request.RedeemAmount;
import com.app.middleware.persistence.type.TransactionType;
import com.app.middleware.resources.services.ChargeService;
import com.app.middleware.resources.services.StripeService;
import com.app.middleware.resources.services.UserWalletService;
import com.app.middleware.utility.id.PublicIdGenerator;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.BalanceTransaction;
import com.stripe.model.Payout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class UserWalletServiceImpl implements UserWalletService {

    @Autowired
    private StripeService stripeService;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private UserWalletRepository userWalletRepository;

    @Autowired
    private UserTransactionsRepository userTransactionsRepository;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Payout subtractMoneyAndSendItToUserPaymentOption(RedeemAmount redeemAmount, User user) throws StripeException {
        return stripeService.subtractMoneyAndSendItToUserPaymentOption(redeemAmount, user);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public UserWallet getWalletDetails(User user) {
        UserWallet userWallet = userWalletRepository.findByUserPublicId(user.getPublicId());
        if(userWallet == null && user.getIsDeleted() == false){
            UserWallet wallet = new UserWallet();
            wallet.setAmount(0d);
            wallet.setCurrency("USD");
            wallet.setPublicId(PublicIdGenerator.generatePublicId());
            wallet.setUser(user);
            return  wallet;
        }
        return userWallet;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public List<UserTransactions> getAllUserTransactions(User user) {
        List<UserTransactions> userTransactions = userTransactionsRepository.findAllByUserPublicId(user.getPublicId());
        return userTransactions;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public UserTransactionBreakdownDTO getTransactionByUserTransactionId(User user, String userTransactionPublicId) throws ResourceNotFoundException, StripeException {
        UserTransactions userTransaction = userTransactionsRepository.findByPublicId(PublicIdGenerator.decodePublicId(userTransactionPublicId));
        if(userTransaction == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_TRANSACTION_NOT_FOUND_WITH_USER_PUBLIC_ID, userTransactionPublicId);

        BalanceTransaction balanceTransaction = new BalanceTransaction();
        if(userTransaction.getTransactionType().equals(TransactionType.DEBIT.toString())){
            balanceTransaction = stripeService.getBalanceTransactionDebit(userTransaction.getPaymentId());
        } else if(userTransaction.getTransactionType().equals(TransactionType.CREDIT.toString())){
            balanceTransaction = stripeService.getBalanceTransactionCredit(userTransaction.getPaymentId());
        }

        UserTransactionBreakdownDTO userTransactionBreakdownDTO = UserTransactionBreakdownDTO.builder()
                .userTransactionDTO(UserTransactionMapper.createUserTransactionDTOLazy(userTransaction))
                .balanceTransactionDTO(BalanceTransactionMapper.createBalanceTransactionDTO(balanceTransaction))
                .build();
        return userTransactionBreakdownDTO;
    }

    @Override
    public Account createConnectAccount(User user) throws StripeException {
        CreateStripeConnectRequest stripeConnectRequest = new CreateStripeConnectRequest();
        stripeConnectRequest.setIp(user.getIp());
        stripeConnectRequest.setDob(user.getDob());
        stripeConnectRequest.setAddressLine(user.getAddressLine());
        stripeConnectRequest.setPostalCode(user.getPostalCode());
        stripeConnectRequest.setCity(user.getCity());
        stripeConnectRequest.setState(user.getState());
        stripeConnectRequest.setCountry(user.getCountry());
        stripeConnectRequest.setFirstName(user.getFirstName());
        stripeConnectRequest.setLastName(user.getLastName());
        stripeConnectRequest.setEmail(user.getEmail());
        stripeConnectRequest.setUserPublicId(user.getPublicId());
        return stripeService.createStripeCustomConnectAccount(stripeConnectRequest);
    }

    @Override
    public Account getConnectAccount(String accountId) throws StripeException {
        return stripeService.getConnectAccount(accountId);
    }

    @Override
    public Account setDefaultCurrencyAccount(String userConnectId, String connectSourceId, boolean isCard, User user) throws StripeException {
        return stripeService.setDefaultCurrencyAccount(userConnectId, connectSourceId, isCard, user);
    }

    @Override
    public Map<String, Object> getBalance(User user) throws StripeException {
        return stripeService.retrieveBalance(user);
    }

    @Override
    public Account setPaymentFrequency(User user, String interval, int weeklyAnchor, int monthlyAnchor, int delayDays) throws StripeException {
        return stripeService.setPaymentFrequency(user, interval, weeklyAnchor, monthlyAnchor,delayDays);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Charge payMoney(PayAmountRequest payAmountRequest, User user) throws com.app.middleware.exceptions.type.ResourceNotFoundException, StripeException, UnauthorizedException {
        Charge charge = chargeService.getChargeByPublicId(PublicIdGenerator.decodePublicId(payAmountRequest.getChargePublicId()));
        charge = stripeService.chargeMoney(charge, user, payAmountRequest);
        return charge;
    }

}
