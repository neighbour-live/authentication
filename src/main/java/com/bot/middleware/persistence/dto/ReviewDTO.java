package com.bot.middleware.persistence.dto;

import com.bot.middleware.persistence.domain.Task;
import com.bot.middleware.persistence.domain.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDTO {
    private String publicId;
    private String taskerName;
    private String posterName;
    private String taskerImage;
    private String posterImage;
    private String taskerReview;
    private String posterReview;
    private int taskerStars;
    private int posterStars;
    private float posterRating;
    private float taskerRating;
    private Boolean isReviewedTasker;
    private Boolean isReviewedPoster;
    private String createDateTime;
    private String updatedDateTime;
}
