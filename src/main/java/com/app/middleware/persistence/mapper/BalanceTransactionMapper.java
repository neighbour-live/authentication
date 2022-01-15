package com.app.middleware.persistence.mapper;
import com.app.middleware.persistence.dto.BalanceTransactionDTO;
import com.stripe.model.BalanceTransaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BalanceTransactionMapper {

    public static BalanceTransactionDTO createBalanceTransactionDTO(BalanceTransaction balanceTransaction) {
        BalanceTransactionDTO balanceTransactionDTO = BalanceTransactionDTO.builder()
                .balanceTransactionId(balanceTransaction.getId())
                .availableOn(balanceTransaction.getAvailableOn())
                .createdOn(balanceTransaction.getCreated())
                .currency(balanceTransaction.getCurrency())
                .description(balanceTransaction.getDescription())
                .exchangeRate(balanceTransaction.getExchangeRate())
                .netAmount(Double.valueOf(balanceTransaction.getNet()/100))
                .paidAmount(Double.valueOf(balanceTransaction.getAmount()/100))
                .status(balanceTransaction.getStatus())
                .feeList(FeeDetailsMapper.createFeeDetailsDTOLazyList(balanceTransaction.getFeeDetails()))
                .build();
        return balanceTransactionDTO;
    }

    public static List<BalanceTransactionDTO> createBalanceTransactionDTOLazy(Collection<BalanceTransaction> balanceTransactions) {
        List<BalanceTransactionDTO> balanceTransactionDTOS = new ArrayList<>();
        balanceTransactions.forEach(balanceTransaction -> balanceTransactionDTOS.add(createBalanceTransactionDTO(balanceTransaction)));
        return balanceTransactionDTOS;
    }
}
