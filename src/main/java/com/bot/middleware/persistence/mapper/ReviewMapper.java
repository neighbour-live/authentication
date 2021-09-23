package com.bot.middleware.persistence.mapper;

import com.bot.middleware.persistence.domain.Review;
import com.bot.middleware.persistence.dto.ReviewDTO;
import com.bot.middleware.utility.id.PublicIdGenerator;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReviewMapper {

    public static ReviewDTO createReviewDTOLazy(Review review) {

        if(review.getIsReviewedPoster() == null ) review.setIsReviewedPoster(false);
        if(review.getIsReviewedTasker() == null ) review.setIsReviewedTasker(false);

        if(review.getIsReviewedPoster() && review.getIsReviewedTasker()){
            //tasker & poster combine case
            ReviewDTO reviewDTO = ReviewDTO.builder()
                    .publicId(PublicIdGenerator.encodedPublicId(review.getPublicId()))
                    .isReviewedPoster(review.getIsReviewedPoster())
                    .isReviewedTasker(review.getIsReviewedTasker())
                    .posterImage(review.getPosterImage())
                    .taskerImage(review.getTaskerImage())
                    .posterName(review.getPosterName())
                    .taskerName(review.getTaskerName())
                    .posterStars(review.getPosterStars())
                    .taskerStars(review.getTaskerStars())
                    .posterReview(review.getPosterReview())
                    .taskerReview(review.getTaskerReview())
                    .posterRating(review.getPoster().getPosterRatingAvg().floatValue())
                    .taskerRating(review.getTasker().getTaskerRatingAvg().floatValue())
                    .createDateTime(review.getCreateDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .updatedDateTime(review.getUpdateDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build();

            return reviewDTO;

        } else if (review.getIsReviewedPoster()){
            //poster only case
            ReviewDTO reviewDTO = ReviewDTO.builder()
                    .publicId(PublicIdGenerator.encodedPublicId(review.getPublicId()))
                    .isReviewedPoster(review.getIsReviewedPoster())
                    .isReviewedTasker(review.getIsReviewedTasker())
                    .posterImage(review.getPosterImage())
                    .posterName(review.getPosterName())
                    .posterStars(review.getPosterStars())
                    .posterReview(review.getPosterReview())
                    .posterRating(review.getPoster().getPosterRatingAvg().floatValue())
                    .createDateTime(review.getCreateDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .updatedDateTime(review.getUpdateDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build();

            return reviewDTO;

        } else {
            //tasker only case
            ReviewDTO reviewDTO = ReviewDTO.builder()
                    .publicId(PublicIdGenerator.encodedPublicId(review.getPublicId()))
                    .isReviewedPoster(review.getIsReviewedPoster())
                    .isReviewedTasker(review.getIsReviewedTasker())
                    .taskerImage(review.getTaskerImage())
                    .taskerName(review.getTaskerName())
                    .taskerStars(review.getTaskerStars())
                    .taskerReview(review.getTaskerReview())
                    .taskerRating(review.getTasker().getTaskerRatingAvg().floatValue())
                    .createDateTime(review.getCreateDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .updatedDateTime(review.getUpdateDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build();

            return reviewDTO;
        }
    }

    public static List<ReviewDTO> createReviewDTOListLazy(Collection<Review> reviews) {
        List<ReviewDTO> reviewDTOS = new ArrayList<>();
        reviews.forEach(review -> reviewDTOS.add(createReviewDTOLazy(review)));
        return reviewDTOS;
    }
}
