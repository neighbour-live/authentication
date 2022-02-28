package com.app.middleware.resources.services;

import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.exceptions.type.UnauthorizedException;
import com.app.middleware.persistence.domain.Charge;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.request.PayAmountRequest;

import java.util.List;

public interface ChargeService {

    Charge createCharge(PayAmountRequest payAmountRequest, User user) throws UnauthorizedException;

    List<Charge> getAllCharges();

    List<Charge> getAllUserCharges(User userPublicId);

    Charge getChargeByPublicId(Long decodePublicId) throws ResourceNotFoundException;
}
