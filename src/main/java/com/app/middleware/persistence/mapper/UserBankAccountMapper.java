package com.app.middleware.persistence.mapper;

import com.app.middleware.persistence.domain.UserBankAccount;
import com.app.middleware.persistence.dto.UserBankAccountDTO;
import com.app.middleware.utility.id.PublicIdGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserBankAccountMapper {

    public static UserBankAccountDTO createUserBankAccountDTOLazy(UserBankAccount userBankAccount) {
        UserBankAccountDTO userBankAccountDTO = UserBankAccountDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(userBankAccount.getPublicId()))
                .stripeSourceId(userBankAccount.getStripeSourceId())
                .bankName(userBankAccount.getBankName())
                .accountHolderName(userBankAccount.getAccountHolderName())
                .transitNumber(userBankAccount.getTransitNumber())
                .financialInstitutionNumber(userBankAccount.getFinancialInstitutionNumber())
                .accountNumber(userBankAccount.getAccountNumber())
                .bankVerified(userBankAccount.getIsVerified())
                .isActive(userBankAccount.getIsActive())
                .isDefault(userBankAccount.getIsDefault())
                .user(UserMapper.createUserMinimalDetailsDTOLazy(userBankAccount.getUser()))
                .build();

        return userBankAccountDTO;
    }

    public static List<UserBankAccountDTO> createUserBankAccountDTOListLazy(Collection<UserBankAccount> userBankAccounts) {
        List<UserBankAccountDTO> userBankAccountDTOS = new ArrayList<>();
        userBankAccounts.forEach(userBankAccount -> userBankAccountDTOS.add(createUserBankAccountDTOLazy(userBankAccount)));
        return userBankAccountDTOS;
    }
}
