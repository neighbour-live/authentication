package com.app.middleware.persistence.mapper;

import com.app.middleware.persistence.dto.BalanceDTO;
import com.stripe.model.Account;
import com.stripe.model.Balance;

public class BalanceMapper {
    public static BalanceDTO createBalanceDTOLazy(Balance balance, Account.PayoutSchedule paymentFrequency) {
        BalanceDTO balanceDTO = BalanceDTO.builder()
                .available(balance.getAvailable().get(0).getAmount())
                .pending(balance.getPending().get(0).getAmount())
                .delayDays(paymentFrequency.getDelayDays())
                .interval(paymentFrequency.getInterval())
                .monthlyAnchor(paymentFrequency.getMonthlyAnchor())
                .weeklyAnchor(paymentFrequency.getWeeklyAnchor())
                .build();

        return balanceDTO;
    }
}
