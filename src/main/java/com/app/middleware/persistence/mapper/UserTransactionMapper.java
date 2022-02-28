package com.app.middleware.persistence.mapper;

import com.app.middleware.persistence.domain.UserTransactions;
import com.app.middleware.persistence.dto.UserTransactionsDTO;
import com.app.middleware.utility.id.PublicIdGenerator;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserTransactionMapper {

    public static UserTransactionsDTO createUserTransactionDTOLazy(UserTransactions userTransactions) {
        UserTransactionsDTO userTransactionsDTO = UserTransactionsDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(userTransactions.getPublicId()))
                .paymentId(userTransactions.getPaymentId())
                .transactionType(userTransactions.getTransactionType().toString())
                .amount(userTransactions.getAmount())
                .purpose(userTransactions.getPurpose())
                .category(userTransactions.getCategory())
                .description(userTransactions.getDescription())
                .userPublicId(PublicIdGenerator.encodedPublicId(userTransactions.getUser().getPublicId()))
                .createDateTime(userTransactions.getCreateDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .updatedDateTime(userTransactions.getUpdateDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        return userTransactionsDTO;
    }

    public static List<UserTransactionsDTO> createUserTransactionDTOListLazy(Collection<UserTransactions> userTransactions) {
        List<UserTransactionsDTO> userTransactionsDTO = new ArrayList<>();
        userTransactions.forEach(userTransaction -> userTransactionsDTO.add(createUserTransactionDTOLazy(userTransaction)));
        return userTransactionsDTO;
    }
}
