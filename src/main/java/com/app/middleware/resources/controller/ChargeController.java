package com.app.middleware.resources.controller;

import com.app.middleware.exceptions.ExceptionUtil;
import com.app.middleware.persistence.domain.Charge;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.mapper.ChargeMapper;
import com.app.middleware.persistence.request.PayAmountRequest;
import com.app.middleware.persistence.response.GenericResponseEntity;
import com.app.middleware.persistence.type.RoleType;
import com.app.middleware.resources.services.AuthorizationService;
import com.app.middleware.resources.services.ChargeService;
import com.app.middleware.utility.StatusCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/charge")
public class ChargeController {

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private ChargeService chargeService;

    @GetMapping("/{userPublicId}")
    @ApiOperation(value = "This operation is used to fetch all the charges.")
    public ResponseEntity<?> getAllCharges(@PathVariable String userPublicId) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);
            List<Charge> charges = null;
            if(user.getRole().getRoleType().equals(RoleType.USER)){
                charges = chargeService.getAllUserCharges(user);
            } else {
                charges = chargeService.getAllCharges();
            }
            return GenericResponseEntity.create(StatusCode.SUCCESS, ChargeMapper.createChargeDTOListLazy(charges), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PostMapping
    @ApiOperation(value = "This operation is used to add an charge.")
    public ResponseEntity<?> addCharge(@Valid @RequestBody PayAmountRequest payAmountRequest) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(payAmountRequest.getCreatorPublicId());
            Charge charge = chargeService.createCharge(payAmountRequest, user);
            return GenericResponseEntity.create(StatusCode.SUCCESS, ChargeMapper.createChargeDTOLazy(charge), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

//    @PatchMapping("/{chargePublicId}/user/{userPublicId}")
//    @ApiOperation(value = "This operation is used to delete charge.")
//    public ResponseEntity<?> updateChargeStatus(@PathVariable String userPublicId, @PathVariable String chargePublicId) throws Exception {
//        try {
//            User user = authorizationService.isCurrentUser(userPublicId);
//            Charge charge = chargeService.getChargeByPublicId(PublicIdGenerator.decodePublicId(chargePublicId));
//            int result = chargeService.updateChargeStatus(charge.getPublicId(), user.getPublicId());
//            return GenericResponseEntity.create(StatusMessageDTO.builder()
//                    .message("Charge " +  ((result > 0) ? AuthConstants.EDITED_SUCCESSFULLY : AuthConstants.EDITED_FAILED))
//                    .status(0)
//                    .build(), HttpStatus.CREATED);
//        } catch (Exception e) {
//            return ExceptionUtil.handleException(e) ;
//        }
//    }
}
