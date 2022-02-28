package com.app.middleware.resources.services.implementation;

import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.exceptions.type.UnauthorizedException;
import com.app.middleware.persistence.domain.Charge;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.repository.ChargeRepository;
import com.app.middleware.persistence.repository.UserRepository;
import com.app.middleware.persistence.request.PayAmountRequest;
import com.app.middleware.persistence.type.PaymentStatus;
import com.app.middleware.persistence.type.ServiceType;
import com.app.middleware.persistence.type.TransactionType;
import com.app.middleware.resources.services.ChargeService;
import com.app.middleware.utility.id.PublicIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChargeServiceImpl implements ChargeService {

    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Charge createCharge(PayAmountRequest payAmountRequest, User user) throws UnauthorizedException {
        if(!user.getPublicId().equals(PublicIdGenerator.decodePublicId(payAmountRequest.getCreatorPublicId()))){
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION, "User is not authorized to create these charges");
        }

        User payer =  userRepository.findByPublicId(PublicIdGenerator.decodePublicId(payAmountRequest.getPayerPublicId()));
        if(payer == null){
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION, "Charge cannot be created against a non-existing user.");
        }

        Charge charge = new Charge();
        charge.setPaymentStatus(PaymentStatus.UNPAID.toString());
        charge.setTransactionType(TransactionType.DEBIT.name());
        charge.setServiceType(ServiceType.valueOf(payAmountRequest.getServiceType()).name());
        charge.setInternalNotes(payAmountRequest.getDescription());
        charge.setTitle(payAmountRequest.getReason());
        charge.setDescription(payAmountRequest.getDescription());
        charge.setStripeChargeId("");

        charge.setCreator(user);
        charge.setPayer(payer);
        charge.setIsActive(true);
        charge.setIsDeleted(false);
        charge.setIsPaid(false);

        charge = chargeRepository.save(charge);
        return charge;
    }

    @Override
    public List<Charge> getAllCharges() {
        return chargeRepository.findAll();
    }

    @Override
    public List<Charge> getAllUserCharges(User user) {
        List<Charge> charges = chargeRepository.findByPayerOrCreatorAndIsDeletedFalse(user);
        return charges;
    }

    public Charge getChargeByPublicId(Long chargePublicId) throws ResourceNotFoundException {
        Charge charge = chargeRepository.findByPublicId(chargePublicId);
        if(charge == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.CHARGE_NOT_FOUND_WITH_CHARGE_PUBLIC_ID);
        return charge;
    }
}
