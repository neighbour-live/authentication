package com.app.middleware.resources.controller;

import com.app.middleware.exceptions.ExceptionUtil;
import com.app.middleware.persistence.domain.Award;
import com.app.middleware.persistence.domain.Faq;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserAward;
import com.app.middleware.persistence.dto.StatusMessageDTO;
import com.app.middleware.persistence.mapper.AwardMapper;
import com.app.middleware.persistence.mapper.FaqMapper;
import com.app.middleware.persistence.mapper.UserAwardsMapper;
import com.app.middleware.persistence.request.AddAward;
import com.app.middleware.persistence.request.AddFaq;
import com.app.middleware.persistence.request.AddUserAward;
import com.app.middleware.persistence.response.GenericResponseEntity;
import com.app.middleware.persistence.type.RoleType;
import com.app.middleware.resources.services.AuthorizationService;
import com.app.middleware.resources.services.AwardsService;
import com.app.middleware.resources.services.FaqsService;
import com.app.middleware.utility.AuthConstants;
import com.app.middleware.utility.StatusCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/faq")
public class FaqController {

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private FaqsService faqsService;

    @GetMapping("/{userPublicId}")
    @PreAuthorize("hasRole('USER') OR hasRole('MODERATOR') OR hasRole('ADMIN')")
    @ApiOperation(value = "This operation is used to fetch all the faq.")
    public ResponseEntity<?> getAllAward(@PathVariable String userPublicId) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);
            List<Faq> faqs = new ArrayList<>();
            if(user.getRole().getRoleType().equals(RoleType.USER)){
                faqs = faqsService.getAllFaqs();
            } else {
                faqs = faqsService.getAllFaqsForApp();
            }
            return GenericResponseEntity.create(StatusCode.SUCCESS, FaqMapper.createFaqDTOListLazy(faqs), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('MODERATOR') OR hasRole('ADMIN')")
    @ApiOperation(value = "This operation is used to add an faq.")
    public ResponseEntity<?> addFaq(@Valid @RequestBody AddFaq addFaq) throws Exception {
        try {
            authorizationService.isCurrentUser(addFaq.getUserPublicId());
            Faq faq = faqsService.createFaq(addFaq);
            return GenericResponseEntity.create(StatusCode.SUCCESS, FaqMapper.createFaqDTOLazy(faq), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PatchMapping()
    @PreAuthorize("hasRole('MODERATOR') OR hasRole('ADMIN')")
    @ApiOperation(value = "This operation is used to delete faq.")
    public ResponseEntity<?> deleteFaq(@PathVariable String userPublicId, @PathVariable String faqPublicId) throws Exception {
        try {
            authorizationService.isCurrentUser(userPublicId);
            faqsService.deleteFaq(faqPublicId);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("Faq " + AuthConstants.DELETED_SUCCESSFULLY)
                    .status(0)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }


    @PutMapping()
    @PreAuthorize("hasRole('MODERATOR') OR hasRole('ADMIN')")
    @ApiOperation(value = "This operation is used to update faq.")
    public ResponseEntity<?> updateFaq(AddFaq addFaq) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(addFaq.getUserPublicId());
            faqsService.editFaq(addFaq, user);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("Faq " + AuthConstants.EDITED_SUCCESSFULLY)
                    .status(0)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }
}
