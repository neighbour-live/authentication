package com.bot.middleware.resources.services.implementation;

import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.bot.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.Review;
import com.bot.middleware.persistence.domain.Task;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.repository.ReviewRepository;
import com.bot.middleware.persistence.repository.UserRepository;
import com.bot.middleware.persistence.request.AddReview;
import com.bot.middleware.persistence.type.TaskStatus;
import com.bot.middleware.resources.services.ReviewService;
import com.bot.middleware.resources.services.TaskingService;
import com.bot.middleware.utility.id.PublicIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    TaskingService taskingService;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    UserRepository userRepository;

    private static final double RATING_DIVISOR = 5.0;

    @Override
    public Review createReview(AddReview addReview, User user) throws UnauthorizedException, ResourceNotFoundException {
        Task task = taskingService.getByTaskPublicId(addReview.getTaskPublicId());
        //checking if user works on task
        if(!task.getTasker().getPublicId().equals(user.getPublicId()) && !task.getPoster().getPublicId().equals(user.getPublicId())){
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
        }

        if(!task.getTaskStatus().equalsIgnoreCase(TaskStatus.COMPLETE.toString())){
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
        }

        Review review = new Review();
        review.setPublicId(PublicIdGenerator.generatePublicId());
        review.setTask(task);
        review.setIsRemoved(false);
        review.setIsDeletedPoster(false);
        review.setIsDeletedTasker(false);
        review.setIsReportedByPoster(false);
        review.setIsReportedByTasker(false);

        //checking if user is poster
        if(task.getPoster().getPublicId().equals(user.getPublicId())){
            review.setIsReviewedPoster(true);
            if(review.getIsReviewedTasker() == null) {
                review.setIsReviewedTasker(false);
            };

            review.setPosterImage(user.getImageUrl());
            review.setPosterName(user.getFirstName() + " " + user.getLastName());
            review.setPosterReview(addReview.getReview());
            review.setPosterStars(addReview.getStars());
            float posterRatingSum = getPosterRatingSum(user.getPublicId());
            int countPosterRatingSum = getPosterRatingCount(user.getPublicId());
            user.setPosterRatingAvg((posterRatingSum + addReview.getStars())/(countPosterRatingSum + 1.0));
            review.setPoster(user);
        }
        //checking if user is labour
        if(task.getTasker().getPublicId().equals(user.getPublicId())){
            review.setIsReviewedTasker(true);
            if(review.getIsReviewedPoster() == null) {
                review.setIsReviewedPoster(false);
            };

            review.setTaskerImage(user.getImageUrl());
            review.setTaskerName(user.getFirstName() + " " + user.getLastName());
            review.setTaskerReview(addReview.getReview());
            review.setTaskerStars(addReview.getStars());
            float taskerRatingSum = getTaskerRatingSum(user.getPublicId());
            int countTaskerRatingSum = getTaskerRatingCount(user.getPublicId());
            user.setTaskerRatingAvg((taskerRatingSum + addReview.getStars())/(countTaskerRatingSum + 1.0));
            review.setTasker(user);
        }
        
        userRepository.save(user);
        return reviewRepository.save(review);
    }

    @Override
    public Review editReview(AddReview addReview, String reviewPublicId) {
        return null;
    }

    @Override
    public List<Review> getAllReview(User user, boolean tasking, boolean sortByRating, boolean sortByRecency) {
        List<Review> reviews = new ArrayList<>();
        if(tasking){
            if(sortByRecency && sortByRating){
                reviews = reviewRepository.findAllByTaskerSortByRecencyAndRating(user.getPublicId());
            } else if(sortByRating){
                reviews = reviewRepository.findAllByTaskerSortByRating(user.getPublicId());
            } else if(sortByRecency) {
                reviews = reviewRepository.findAllByTaskerSortByRecency(user.getPublicId());
            }
        } else {
            if(sortByRecency && sortByRating){
                reviews = reviewRepository.findAllByPosterSortByRecencyAndRating(user.getPublicId());
            } else if(sortByRating){
                reviews = reviewRepository.findAllByPosterSortByRating(user.getPublicId());
            } else if(sortByRecency){
                reviews = reviewRepository.findAllByPosterSortByRecency(user.getPublicId());
            }
        }

        return reviews;
    }

    @Override
    public Review getByReviewId(String reviewPublicId, String user) throws ResourceNotFoundException {
        Review review = reviewRepository.findByPublicId(PublicIdGenerator.decodePublicId(reviewPublicId));
        if(review == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.REVIEW_NOT_FOUND_WITH_PUBLIC_ID, reviewPublicId);
        return review;
    }

    @Override
    public Review deleteUserReview(String reviewPublicId, String userPublicId) {
        return null;
    }

    private float getTaskerRatingSum(Long publicId) {
        return reviewRepository.getTaskerRatingSum(publicId);
    }

    private float getPosterRatingSum(Long publicId) {
        return reviewRepository.getPosterRatingSum(publicId);
    }

    private int getTaskerRatingCount(Long publicId) {
        return reviewRepository.getTaskerRatingCount(publicId);
    }

    private int getPosterRatingCount(Long publicId) {
        return reviewRepository.getPosterRatingCount(publicId);
    }
}
