package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserBid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public interface UserBidRepository extends JpaRepository<UserBid,Long> {

    @Query(value = "SELECT b FROM UserBid b LEFT JOIN FETCH b.user u LEFT JOIN FETCH b.task t LEFT JOIN FETCH t.poster tp " +
            "WHERE u.isDeleted = false AND b.isDeleted = false AND b.publicId = :decodePublicId ")
    UserBid findByPublicId(Long decodePublicId);

    @Query(value = "SELECT b FROM UserBid b LEFT JOIN FETCH b.user u LEFT JOIN FETCH b.task t LEFT JOIN FETCH t.poster tp " +
            "WHERE u.isDeleted = false AND b.isDeleted = false AND u.publicId = :decodePublicId ")
    List<UserBid> findAllByUserPublicId(Long decodePublicId);

    @Query(value = "SELECT count(ub) FROM UserBid ub WHERE ub.isDeleted = false " +
            "AND ub.user.publicId =:userPublicId AND ub.task.publicId =:taskPublicId ")
    int checkBidAlreadyExist(Long userPublicId, Long taskPublicId);

    @Query(value = "SELECT ub FROM UserBid ub LEFT JOIN FETCH ub.task t LEFT JOIN FETCH t.poster tp WHERE t.isDeleted = false AND ub.isDeleted = false " +
            "AND t.publicId = :decodePublicId")
    List<UserBid> findAllTaskBids(Long decodePublicId);

    @Query(value = "SELECT ub FROM UserBid ub LEFT JOIN FETCH ub.task t LEFT JOIN FETCH t.tasker tt WHERE t.isDeleted = false AND ub.isDeleted = false " +
            "AND t.startDateTime >= :dayStart AND t.endDateTime <= :dayEnd AND tt.publicId = :publicId")
    List<UserBid> checkBidderBookingsForDay(Long publicId, ZonedDateTime dayStart, ZonedDateTime dayEnd);
}
