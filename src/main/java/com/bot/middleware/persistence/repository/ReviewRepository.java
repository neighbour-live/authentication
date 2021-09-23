package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.ReportUser;
import com.bot.middleware.persistence.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    Review findByPublicId(Long decodePublicId);

    @Query(value = "SELECT COALESCE(sum(r.posterStars), 0.0) FROM Review r WHERE r.isRemoved = false AND r.poster.publicId = :publicId")
    float getPosterRatingSum(Long publicId);

    @Query(value = "SELECT COALESCE(sum(r.taskerStars), 0.0) FROM Review r WHERE r.isRemoved = false AND r.tasker.publicId = :publicId")
    float getTaskerRatingSum(Long publicId);

    @Query(value = "SELECT COALESCE(count(r.posterStars), 0) FROM Review r WHERE r.isRemoved = false AND r.poster.publicId = :publicId")
    int getPosterRatingCount(Long publicId);

    @Query(value = "SELECT COALESCE(count(r.taskerStars), 0) FROM Review r WHERE r.isRemoved = false AND r.tasker.publicId = :publicId")
    int getTaskerRatingCount(Long publicId);

    @Query(value = "SELECT r FROM Review r where r.isRemoved = false and r.poster.publicId = :publicId GROUP BY r ORDER BY r.posterStars, r.createDateTime")
    List<Review> findAllByPosterSortByRecencyAndRating(Long publicId);

    @Query(value = "SELECT r FROM Review r where r.isRemoved = false and r.tasker.publicId = :publicId GROUP BY r ORDER BY r.taskerStars, r.createDateTime")
    List<Review> findAllByTaskerSortByRecencyAndRating(Long publicId);

    @Query(value = "SELECT r FROM Review r where r.isRemoved = false and r.poster.publicId = :publicId GROUP BY r ORDER BY r.createDateTime")
    List<Review> findAllByPosterSortByRecency(Long publicId);

    @Query(value = "SELECT r FROM Review r where r.isRemoved = false and r.tasker.publicId = :publicId GROUP BY r ORDER BY r.createDateTime")
    List<Review> findAllByTaskerSortByRecency(Long publicId);

    @Query(value = "SELECT r FROM Review r where r.isRemoved = false and r.poster.publicId = :publicId GROUP BY r ORDER BY r.posterStars")
    List<Review> findAllByPosterSortByRating(Long publicId);

    @Query(value = "SELECT r FROM Review r where r.isRemoved = false and r.tasker.publicId = :publicId GROUP BY r ORDER BY r.taskerStars")
    List<Review> findAllByTaskerSortByRating(Long publicId);

}
