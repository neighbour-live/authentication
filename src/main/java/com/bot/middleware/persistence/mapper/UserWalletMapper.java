package com.bot.middleware.persistence.mapper;

import com.bot.middleware.persistence.domain.UserTransactions;
import com.bot.middleware.persistence.domain.UserWallet;
import com.bot.middleware.persistence.dto.UserTransactionsDTO;
import com.bot.middleware.persistence.dto.UserWalletDTO;
import com.bot.middleware.utility.id.PublicIdGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserWalletMapper {

    public static UserWalletDTO createUserWalletDTOLazy(UserWallet userWallet) {
        UserWalletDTO userWalletDTO = UserWalletDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(userWallet.getPublicId()))
                .currency(userWallet.getCurrency())
                .amount(userWallet.getAmount())
                .userPublicId(PublicIdGenerator.encodedPublicId(userWallet.getUser().getPublicId()))
                .build();

        return userWalletDTO;
    }

    public static List<UserWalletDTO> createUserWalletDTOListLazy(Collection<UserWallet> userWallets) {
        List<UserWalletDTO> userWalletDTOS = new ArrayList<>();
        userWallets.forEach(userWallet -> userWalletDTOS.add(createUserWalletDTOLazy(userWallet)));
        return userWalletDTOS;
    }
}
