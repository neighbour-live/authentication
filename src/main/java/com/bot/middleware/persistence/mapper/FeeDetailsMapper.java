package com.bot.middleware.persistence.mapper;

import com.bot.middleware.persistence.dto.FeeDetailsDTO;
import com.stripe.model.BalanceTransaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FeeDetailsMapper {

    public static FeeDetailsDTO createAwardDTOLazy(BalanceTransaction.Fee fee) {
        FeeDetailsDTO feeDetailsDTO = FeeDetailsDTO.builder()
                .amount(Double.valueOf(fee.getAmount()/100))
                .currency(fee.getCurrency())
                .description(fee.getDescription())
                .feeType(fee.getType())
                .build();

        return feeDetailsDTO;
    }

    public static List<FeeDetailsDTO> createAwardDTOListLazy(List<BalanceTransaction.Fee> fees) {
        List<FeeDetailsDTO> feeDetailsDTOS = new ArrayList<>();
        fees.forEach(fee -> feeDetailsDTOS.add(createAwardDTOLazy(fee)));
        return feeDetailsDTOS;
    }
}
