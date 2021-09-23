package com.bot.middleware.resources.controller;

import com.bot.middleware.exceptions.ExceptionUtil;
import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.bot.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.Review;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.mapper.ReviewMapper;
import com.bot.middleware.persistence.repository.UserRepository;
import com.bot.middleware.persistence.request.AddReview;
import com.bot.middleware.persistence.response.GenericResponseEntity;
import com.bot.middleware.resources.services.ReviewService;
import com.bot.middleware.security.UserPrincipal;
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
@RequestMapping("/review")
public class ReviewsController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to add review.")
    public ResponseEntity<?> addReview(@Valid @RequestBody AddReview addReview) throws Exception {
        try {
            User user = isCurrentUser(addReview.getUserPublicId());
            Review review = reviewService.createReview(addReview, user);
            return GenericResponseEntity.create(StatusCode.SUCCESS, ReviewMapper.createReviewDTOLazy(review), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch user all reviews.")
    public ResponseEntity<?> getAllUserReviews(
            @RequestParam("userPublicId") String userPublicId,
            @RequestParam("tasking") boolean tasking,
            @RequestParam("sortByRating") boolean sortByRating,
            @RequestParam("sortByRecency") boolean sortByRecency
    ) throws Exception {
        try {
            User user = isCurrentUser(userPublicId);
            List<Review> reviews = reviewService.getAllReview(user, tasking, sortByRating, sortByRecency);
            return GenericResponseEntity.create(StatusCode.SUCCESS, ReviewMapper.createReviewDTOListLazy(reviews), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/{reviewPublicId}/user/{userPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch specific review.")
    public ResponseEntity<?> getByReviewId(@PathVariable String reviewPublicId, @PathVariable String userPublicId) throws Exception {
        try {
            isCurrentUser(userPublicId);
            Review review = reviewService.getByReviewId(reviewPublicId, userPublicId);
            return GenericResponseEntity.create(StatusCode.SUCCESS, ReviewMapper.createReviewDTOLazy(review), HttpStatus.OK);
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
