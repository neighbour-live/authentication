package com.app.middleware.persistence.mapper;

import com.app.middleware.persistence.domain.Charge;
import com.app.middleware.persistence.dto.ChargeDTO;
import com.app.middleware.utility.id.PublicIdGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChargeMapper {

    public static ChargeDTO createChargeDTOLazy(Charge charge) {
        ChargeDTO chargeDTO = ChargeDTO.builder()
                .chargePublicId(PublicIdGenerator.encodedPublicId(charge.getPublicId()))
                .chargeStripeId(charge.getStripeChargeId())
                .paymentStatus(charge.getPaymentStatus())
                .serviceType(charge.getServiceType())
                .transactionType(charge.getTransactionType())
                .internalNotes(charge.getInternalNotes())
                .description(charge.getDescription())
                .title(charge.getTitle())
                .chargeCreator(UserMapper.createUserMinimalDetailsDTOLazy(charge.getCreator()))
                .chargePayer(UserMapper.createUserMinimalDetailsDTOLazy(charge.getPayer()))
                .build();

        return chargeDTO;
    }

    public static List<ChargeDTO> createChargeDTOListLazy(Collection<Charge> charges) {
        List<ChargeDTO> chargeDTOS = new ArrayList<>();
        charges.forEach(charge -> chargeDTOS.add(createChargeDTOLazy(charge)));
        return chargeDTOS;
    }
}
