package com.bot.middleware.resources.services;

import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserPaymentCard;
import com.bot.middleware.persistence.request.AddPaymentCardRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.Card;

import java.util.List;

public interface UserPaymentCardService {
    UserPaymentCard addPaymentCard(AddPaymentCardRequest addPaymentCardRequest, String publicId, String userConnectId) throws StripeException, ResourceNotFoundException, UnauthorizedException;

    boolean checkCardExist(Long card);

    List<UserPaymentCard> getAllUserPaymentCard(User user);

    boolean deleteUserPaymentCard(String connectCardPublicId, User user) throws ResourceNotFoundException, StripeException;

    UserPaymentCard editPaymentCard(AddPaymentCardRequest addPaymentCardRequest, User user, String userConnectId) throws ResourceNotFoundException;
}
