package com.bot.middleware.persistence.mapper;

import com.bot.middleware.persistence.domain.UserBid;
import com.bot.middleware.persistence.dto.UserBidDTO;
import com.bot.middleware.utility.id.PublicIdGenerator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class UserBidMapper {

    public static UserBidDTO createBidDTOLazy(UserBid userBid) {
        UserBidDTO userBidDTO = UserBidDTO.builder()
                .budget(BigInteger.valueOf((Math.round(userBid.getBudget()))))
                .otherCosts(BigInteger.valueOf((Math.round(userBid.getOtherCosts()))))
                .hours(userBid.getHours())
                .hourlyRate((int) userBid.getHourlyRate())
                .imageUrl(userBid.getImageUrl())
                .status(userBid.getStatus())
                .description(userBid.getDescription())
                .otherCostsExplanation(userBid.getOtherCostsExplanation())
                .timeUtilizationExplanation(userBid.getTimeUtilizationExplanation())
                .publicId(PublicIdGenerator.encodedPublicId(userBid.getPublicId()))
                .bidder(UserMapper.createUserMinimalDetailsDTOLazy(userBid.getUser()))
                .task(TaskMapper.createTaskDTOLazy(userBid.getTask(), userBid.getUser()))
                .taskPublicId(PublicIdGenerator.encodedPublicId(userBid.getTask().getPublicId()))
                .build();

        return userBidDTO;
    }

    public static List<UserBidDTO> createBidDTOListLazy(Collection<UserBid> userBids) {
        List<UserBidDTO> userBidDTOS = new ArrayList<>();
        userBids.forEach(userBid -> userBidDTOS.add(createBidDTOLazy(userBid)));
        return userBidDTOS;
    }
}
