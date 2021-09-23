package com.bot.middleware.resources.services;

import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserBid;
import com.bot.middleware.persistence.request.AddUserBidRequest;

import java.util.List;

public interface BiddingService {
    UserBid addBid(AddUserBidRequest addUserBidRequest, User user) throws Exception;

    UserBid editBid(AddUserBidRequest addUserBidRequest, String bidPublicId, User user, String status) throws ResourceNotFoundException, UnauthorizedException;

    List<UserBid> getAllBids(String userPublicId);

    UserBid getByBidId(String bidPublicId) throws ResourceNotFoundException;

    boolean deleteBid(String bidPublicId, User userPublicId) throws ResourceNotFoundException, Exception;

    UserBid acceptRejectBid(User user, String taskPublicId, String bidPublicId, boolean accept) throws Exception;
}
