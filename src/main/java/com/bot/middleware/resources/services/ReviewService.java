package com.bot.middleware.resources.services;

import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.Review;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.request.AddReview;

import java.util.List;

public interface ReviewService {

    Review createReview(AddReview addReview, User user) throws UnauthorizedException, ResourceNotFoundException;

    Review editReview(AddReview addReview, String reviewPublicId);

    Review getByReviewId(String reviewPublicId, String user) throws ResourceNotFoundException;

    Review deleteUserReview(String reviewPublicId, String userPublicId);

    List<Review> getAllReview(User user, boolean tasking, boolean sortByRating, boolean sortByRecency);
}
