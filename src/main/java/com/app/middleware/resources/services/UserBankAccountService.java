package com.app.middleware.resources.services;

import com.app.middleware.exception.ResourceNotFoundException;
import com.app.middleware.exceptions.type.UnauthorizedException;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserBankAccount;
import com.app.middleware.persistence.request.AddBankAccountRequest;
import com.stripe.exception.StripeException;

import java.util.List;

public interface UserBankAccountService {

    List<UserBankAccount> getAllBankAccounts(User user);

    UserBankAccount findByUser(User user) throws ResourceNotFoundException, com.app.middleware.exceptions.type.ResourceNotFoundException;

    boolean findIfBankVerified(User user) throws ResourceNotFoundException;

    boolean deleteBankAccount(String bankAccountPublicId, User user) throws ResourceNotFoundException, StripeException;

    UserBankAccount addBankAccount(AddBankAccountRequest addBankAccountRequest, String publicId, String connectId) throws StripeException, ResourceNotFoundException, UnauthorizedException;

    UserBankAccount verifyBankAccount(String publicId, String stripeId, String stripeBankId) throws ResourceNotFoundException, StripeException;

    boolean checkBankAccountExist(String accountNumber);

    UserBankAccount editBankAccount(String bankAccountPublicId, User user, AddBankAccountRequest addBankAccountRequest);
}
