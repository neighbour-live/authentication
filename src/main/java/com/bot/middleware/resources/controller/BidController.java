package com.bot.middleware.resources.controller;

import com.bot.middleware.exceptions.ExceptionUtil;
import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.bot.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserBid;
import com.bot.middleware.persistence.dto.StatusMessageDTO;
import com.bot.middleware.persistence.mapper.UserBidMapper;
import com.bot.middleware.persistence.repository.UserRepository;
import com.bot.middleware.persistence.request.AddUserBidRequest;
import com.bot.middleware.persistence.response.GenericResponseEntity;
import com.bot.middleware.resources.services.BiddingService;
import com.bot.middleware.security.UserPrincipal;
import com.bot.middleware.utility.AuthConstants;
import com.bot.middleware.utility.StatusCode;
import com.bot.middleware.utility.id.PublicIdGenerator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/bid")
public class BidController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BiddingService biddingService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to add id against Task.")
    public ResponseEntity<?> addBid(@RequestParam("userPublicId") String userPublicId, @Valid @RequestBody AddUserBidRequest addUserBidRequest) throws Exception {
        try {
            User user = isCurrentUser(userPublicId);
            UserBid userBid = biddingService.addBid(addUserBidRequest, user);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserBidMapper.createBidDTOLazy(userBid), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PutMapping("/{bidPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to edit bid.")
    public ResponseEntity<?> editBid(@Valid @RequestBody AddUserBidRequest addUserBidRequest, @PathVariable String bidPublicId) throws Exception {
        try {
            User user = isCurrentUser(addUserBidRequest.getUserPublicId());
            UserBid userBid = biddingService.editBid(addUserBidRequest, bidPublicId, user, null);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("User Bid " + AuthConstants.EDITED_SUCCESSFULLY)
                    .status(0)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PutMapping("/status/{bidPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to accept/reject bid.")
    public ResponseEntity<?> acceptRejectBid(@RequestParam("userPublicId") String userPublicId,
                                             @RequestParam("taskPublicId") String taskPublicId,
                                             @RequestParam(value = "accept", defaultValue = "true") boolean accept,
                                             @PathVariable String bidPublicId) throws Exception {
        try {
            User user = isCurrentUser(userPublicId);
            UserBid userBid = biddingService.acceptRejectBid(user, taskPublicId, bidPublicId, accept);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("User Bid " + AuthConstants.EDITED_SUCCESSFULLY)
                    .status(0)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to get all the user bids.")
    public ResponseEntity<?> getAllBids(@RequestParam("userPublicId") String userPublicId) throws Exception {
        try {
            isCurrentUser(userPublicId);
            List<UserBid> userBids = biddingService.getAllBids(userPublicId);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserBidMapper.createBidDTOListLazy(userBids), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/{bidPublicId}/user/{userPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch detail of specific bid.")
    public ResponseEntity<?> getByBidId(@PathVariable String bidPublicId, @PathVariable String userPublicId) throws Exception {
        try {
            isCurrentUser(userPublicId);
            UserBid userBid = biddingService.getByBidId(bidPublicId);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserBidMapper.createBidDTOLazy(userBid), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PatchMapping("/{bidPublicId}/user/{userPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to delete bid.")
    public ResponseEntity<?> deleteBid(@PathVariable String bidPublicId, @PathVariable String userPublicId) throws Exception {
        try {
            User user = isCurrentUser(userPublicId);
            biddingService.deleteBid(bidPublicId, user);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("User Bid " + AuthConstants.DELETED_SUCCESSFULLY)
                    .status(0)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    public User isCurrentUser(String userPublicId) throws ResourceNotFoundException, UnauthorizedException {
        User user = userRepository.findByPublicId(PublicIdGenerator.decodePublicId(userPublicId));
        if(user == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH_PUBLIC_ID, userPublicId);

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!userPrincipal.getEmail().equals(user.getEmail())) throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);

        return user;
    }
}
