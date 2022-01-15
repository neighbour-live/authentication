package com.app.middleware.resources.services.implementation;


import com.app.middleware.exception.ResourceNotFoundException;
import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.type.UnauthorizedException;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserPaymentCard;
import com.app.middleware.persistence.repository.UserPaymentCardsRepository;
import com.app.middleware.persistence.repository.UserRepository;
import com.app.middleware.persistence.request.AddPaymentCardRequest;
import com.app.middleware.resources.services.StripeService;
import com.app.middleware.resources.services.UserPaymentCardService;
import com.app.middleware.utility.id.PublicIdGenerator;
import com.stripe.exception.StripeException;
import com.stripe.model.Card;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserPaymentCardServiceImpl implements UserPaymentCardService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserPaymentCardsRepository userPaymentCardsRepository;

    @Autowired
    StripeService stripeService;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public UserPaymentCard addPaymentCard(AddPaymentCardRequest addPaymentCardRequest, String publicId, String userConnectId) throws StripeException, ResourceNotFoundException, UnauthorizedException {

        User user = userRepository.findByPublicId(PublicIdGenerator.decodePublicId(publicId));
        if(user == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH_PUBLIC_ID, publicId);
        UserPaymentCard userPaymentCard = stripeService.addPaymentCard(user, addPaymentCardRequest, userConnectId);
        return userPaymentCard;
    }

    @Override
    public boolean checkCardExist(Long card) {
        User user = userRepository.findByUserPaymentCards(card);
        if(user == null){
            return false;
        }
        return true;
    }

    @Override
    public List<UserPaymentCard> getAllUserPaymentCard(User user) {
        return userPaymentCardsRepository.findAllByUser(user);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean deleteUserPaymentCard(String connectCardPublicId, User user) throws ResourceNotFoundException, StripeException {
        UserPaymentCard userPaymentCard = userPaymentCardsRepository.findByPublicId(PublicIdGenerator.decodePublicId(connectCardPublicId));
        if(userPaymentCard == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_PAYMENT_CARD_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_PAYMENT_CARD, connectCardPublicId);
        stripeService.deleteCard(user, userPaymentCard);
        userPaymentCardsRepository.deleteById(userPaymentCard.getId());
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SneakyThrows
    @Override
    public UserPaymentCard editPaymentCard(AddPaymentCardRequest addPaymentCardRequest, User user, String connectCardPublicId) throws ResourceNotFoundException {
        UserPaymentCard userPaymentCard = userPaymentCardsRepository.findByPublicId(PublicIdGenerator.decodePublicId(connectCardPublicId));
        if(userPaymentCard == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_PAYMENT_CARD_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_PAYMENT_CARD, connectCardPublicId);

        Card card = stripeService.editCard(user, userPaymentCard, addPaymentCardRequest);

        //Updating User Payment Card in DB
        userPaymentCard.setCardholderName(user.getFirstName()+ " " +user.getLastName());
        userPaymentCard.setCardNumber(addPaymentCardRequest.getCardNumber().substring(addPaymentCardRequest.getCardNumber().length() - 4));
        userPaymentCard.setCardExpiryDate(addPaymentCardRequest.getExpMonth()+"/"+addPaymentCardRequest.getExpYear());
        userPaymentCard = userPaymentCardsRepository.save(userPaymentCard);
        userPaymentCardsRepository.deleteById(userPaymentCard.getId());
        return userPaymentCard;
    }
}
