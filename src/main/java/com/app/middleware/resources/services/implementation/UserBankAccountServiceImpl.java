package com.app.middleware.resources.services.implementation;

import com.app.middleware.exception.ResourceNotFoundException;
import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.type.UnauthorizedException;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserBankAccount;
import com.app.middleware.persistence.repository.UserBankAccountRepository;
import com.app.middleware.persistence.repository.UserRepository;
import com.app.middleware.persistence.request.AddBankAccountRequest;
import com.app.middleware.resources.services.StripeService;
import com.app.middleware.resources.services.UserBankAccountService;
import com.app.middleware.utility.id.PublicIdGenerator;
import com.stripe.exception.StripeException;
import com.stripe.model.BankAccount;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserBankAccountServiceImpl implements UserBankAccountService {
    @Autowired
    StripeService stripeService;

    @Autowired
    UserBankAccountRepository userBankAccountRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public UserBankAccount findByUser(User user) throws ResourceNotFoundException {
        UserBankAccount userBankAccount = userBankAccountRepository.findByUser(user);
        if(userBankAccount == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_BANK_ACCOUNT_NOT_FOUND_WITH_USER,
                PublicIdGenerator.encodedPublicId(user.getPublicId()));
        return userBankAccount;
    }

    @Override
    public boolean findIfBankVerified(User user){
        UserBankAccount userBankAccount = userBankAccountRepository.findByUser(user);
        if(userBankAccount == null) return false;
        if(userBankAccount.getIsVerified()) return true;
        return false;
    }

    @Override
    public List<UserBankAccount> getAllBankAccounts(User user) {
        return userBankAccountRepository.findAllByUser(user);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean deleteBankAccount(String bankAccountPublicId, User user) throws ResourceNotFoundException, StripeException {
        UserBankAccount userBankAccount = userBankAccountRepository.findByPublicId(PublicIdGenerator.decodePublicId(bankAccountPublicId));
        if(userBankAccount == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_BANK_ACCOUNT_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_BANK_ACCOUNT, bankAccountPublicId);
        stripeService.deleteBankAccount(user, userBankAccount);
        userBankAccountRepository.delete(userBankAccount);
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public UserBankAccount addBankAccount(AddBankAccountRequest addBankAccountRequest, String publicId, String connectId) throws StripeException, ResourceNotFoundException, UnauthorizedException {
        User user = userRepository.findByPublicId(PublicIdGenerator.decodePublicId(publicId));
        if(user == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH_PUBLIC_ID, publicId);
        UserBankAccount userBankAccount = stripeService.addBankAccount(user, addBankAccountRequest, connectId);
        return userBankAccount;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserBankAccount verifyBankAccount(String publicId, String stripeId, String stripeBankId) throws ResourceNotFoundException, StripeException {
        User user = userRepository.findByPublicId(PublicIdGenerator.decodePublicId(publicId));
        if(user == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH_PUBLIC_ID, publicId);
        UserBankAccount userBankAccount = stripeService.verifyBankAccount(user, stripeId, stripeBankId);
        return userBankAccount;
    }

    @Override
    public boolean checkBankAccountExist(String accountNumber) {
        User user = userRepository.findByUserBankAccounts(accountNumber);
        if(user == null){
            return false;
        }
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SneakyThrows
    @Override
    public UserBankAccount editBankAccount(String bankAccountPublicId, User user, AddBankAccountRequest addBankAccountRequest) {
        UserBankAccount userBankAccount = userBankAccountRepository.findByPublicId(PublicIdGenerator.decodePublicId(bankAccountPublicId));
        if(userBankAccount == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_BANK_ACCOUNT_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_BANK_ACCOUNT, bankAccountPublicId);
        BankAccount bankAccount =  stripeService.editBankAccount(user, userBankAccount, addBankAccountRequest);

        //Editing User Bank account
        userBankAccount.setAccountHolderName(addBankAccountRequest.getAccountHolderName());
        userBankAccount.setBankName(addBankAccountRequest.getBankName());
        userBankAccount.setTransitNumber(addBankAccountRequest.getTransitNumber());
        userBankAccount.setFinancialInstitutionNumber(addBankAccountRequest.getFinancialInstitutionNumber());
        userBankAccount.setAccountNumber(addBankAccountRequest.getAccountNumber().substring(addBankAccountRequest.getAccountNumber().length() - 4));
        userBankAccount = userBankAccountRepository.save(userBankAccount);

        return  userBankAccount;
    }
}
