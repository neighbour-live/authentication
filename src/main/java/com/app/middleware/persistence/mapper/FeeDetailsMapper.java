package com.app.middleware.persistence.mapper;


import com.app.middleware.persistence.dto.FeeDetailsDTO;
import com.stripe.model.BalanceTransaction;

import java.util.ArrayList;
import java.util.List;

public class FeeDetailsMapper {

    public static FeeDetailsDTO createFeeDetailsDTOLazy(BalanceTransaction.Fee fee) {
        FeeDetailsDTO feeDetailsDTO = FeeDetailsDTO.builder()
                .amount(Double.valueOf(fee.getAmount()/100))
                .currency(fee.getCurrency())
                .description(fee.getDescription())
                .feeType(fee.getType())
                .build();

        return feeDetailsDTO;
    }

    public static List<FeeDetailsDTO> createFeeDetailsDTOLazyList(List<BalanceTransaction.Fee> fees) {
        List<FeeDetailsDTO> feeDetailsDTOS = new ArrayList<>();
        fees.forEach(fee -> feeDetailsDTOS.add(createFeeDetailsDTOLazy(fee)));
        return feeDetailsDTOS;
    }
}
