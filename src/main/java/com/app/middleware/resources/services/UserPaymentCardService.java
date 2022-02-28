package com.app.middleware.resources.services;

import com.app.middleware.exception.ResourceNotFoundException;
import com.app.middleware.exceptions.type.UnauthorizedException;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserPaymentCard;
import com.app.middleware.persistence.request.AddPaymentCardRequest;
import com.stripe.exception.StripeException;

import java.util.List;

public interface UserPaymentCardService {
    UserPaymentCard addPaymentCard(AddPaymentCardRequest addPaymentCardRequest, String publicId, String userConnectId) throws StripeException, ResourceNotFoundException, UnauthorizedException;

    boolean checkCardExist(String card);

    List<UserPaymentCard> getAllUserPaymentCard(User user);

    boolean deleteUserPaymentCard(String connectCardPublicId, User user) throws ResourceNotFoundException, StripeException;

    UserPaymentCard editPaymentCard(AddPaymentCardRequest addPaymentCardRequest, User user, String userConnectId) throws ResourceNotFoundException;
}
