package com.app.middleware.persistence.mapper;

import com.app.middleware.persistence.domain.UserPaymentCard;
import com.app.middleware.persistence.dto.UserPaymentCardsDTO;
import com.app.middleware.utility.id.PublicIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPaymentCardMapper {

    @Autowired
    private static PasswordEncoder passwordEncoder;

    public static UserPaymentCardsDTO createUserPaymentCardsDTOLazy(UserPaymentCard userPaymentCards) {
        UserPaymentCardsDTO userPaymentCardsDTO = UserPaymentCardsDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(userPaymentCards.getPublicId()))
                .stripeSourceId(userPaymentCards.getStripeSourceId())
                .connectSourceId(userPaymentCards.getConnectSourceId())
                .cardBrand(userPaymentCards.getCardBrand())
                .cardholderName(userPaymentCards.getCardholderName())
                .cardType(userPaymentCards.getCardType())
                .cardExpiryDate(userPaymentCards.getCardExpiryDate())
                .cardNumber(userPaymentCards.getCardNumber())
                .userPublicId(PublicIdGenerator.encodedPublicId(userPaymentCards.getUser().getPublicId()))
                .isActive(userPaymentCards.getIsActive())
                .isDefault(userPaymentCards.getIsDefault())
                .build();

        return userPaymentCardsDTO;
    }

    public static List<UserPaymentCardsDTO> createUserPaymentCardsDTOListLazy(Collection<UserPaymentCard> userPaymentCards) {
        List<UserPaymentCardsDTO> userPaymentCardsDTOS = new ArrayList<>();
        userPaymentCards.forEach(userPaymentCard -> userPaymentCardsDTOS.add(createUserPaymentCardsDTOLazy(userPaymentCard)));
        return userPaymentCardsDTOS;
    }
}
