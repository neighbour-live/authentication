package com.bot.middleware.resources.services;

import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserBankAccount;
import com.bot.middleware.persistence.request.AddBankAccountRequest;
import com.stripe.exception.StripeException;

import java.util.List;

public interface UserBankAccountService {

    List<UserBankAccount> getAllBankAccounts(User user);

    UserBankAccount findByUser(User user) throws ResourceNotFoundException;

    boolean findIfBankVerified(User user) throws ResourceNotFoundException;

    boolean deleteBankAccount(String bankAccountPublicId, User user) throws ResourceNotFoundException, StripeException;

    UserBankAccount addBankAccount(AddBankAccountRequest addBankAccountRequest, String publicId, String connectId) throws StripeException, ResourceNotFoundException, UnauthorizedException;

    UserBankAccount verifyBankAccount(String publicId, String stripeId, String stripeBankId) throws ResourceNotFoundException, StripeException;

    boolean checkBankAccountExist(String accountNumber);

    UserBankAccount editBankAccount(String bankAccountPublicId, User user, AddBankAccountRequest addBankAccountRequest);
}
