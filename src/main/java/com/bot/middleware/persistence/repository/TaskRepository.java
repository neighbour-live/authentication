package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.Task;
import com.bot.middleware.persistence.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {

    @Query(value = "SELECT t FROM Task t LEFT JOIN FETCH t.taskCategory tc LEFT JOIN FETCH t.tasker tt LEFT JOIN FETCH t.poster tp LEFT JOIN FETCH t.userBids ub WHERE t.isDeleted = false AND t.isApproved = true AND t.poster = :poster ORDER BY t.updateDateTime, t.createDateTime DESC")
    Collection<Task> findAllByPoster(User poster);

    @Query(value = "SELECT t FROM Task t LEFT JOIN FETCH t.taskCategory tc LEFT JOIN FETCH t.tasker tt LEFT JOIN FETCH t.poster tp LEFT JOIN FETCH t.userBids ub WHERE t.isDeleted = false AND t.isApproved = true AND t.taskStatus IN (:statuses) AND t.poster = :poster ORDER BY t.updateDateTime, t.createDateTime DESC")
    Collection<Task> findAllByPosterAndTaskStatus(User poster, List<String> statuses);

    @Query(value = "SELECT t FROM Task t LEFT JOIN FETCH t.taskCategory tc LEFT JOIN FETCH t.tasker tt LEFT JOIN FETCH t.poster tp LEFT JOIN FETCH t.userBids ub WHERE t.isDeleted = false AND t.publicId = :taskPublicId" )
    Task findTaskByTaskPublicId(Long taskPublicId);

    @Query(value = "SELECT t FROM Task t LEFT JOIN FETCH t.taskCategory tc LEFT JOIN FETCH t.tasker tt LEFT JOIN FETCH t.poster tp LEFT JOIN FETCH t.userBids ub WHERE t.publicId =:taskPublicId AND t.poster =:poster AND t.isDeleted = false")
    Task findUndeletedTaskByTaskPublicId(Long taskPublicId, User poster);

    @Query( countQuery = "SELECT COUNT (t) FROM Task t " +
            "LEFT JOIN t.taskCategory tc " +
            "LEFT JOIN t.tasker tt " +
            "LEFT JOIN t.poster tp " +
            "LEFT JOIN t.userBids ub " +
            "LEFT JOIN t.userAddress ta " +
            "WHERE (lower(t.taskStatus) like lower('%APPROVED%') ) " +
            "AND (t.isDeleted = false) " +
            "AND t.poster <> :user " +
            "AND (:lowBudget is NULL OR t.budget >= :lowBudget) " +
            "AND (:highBudget is NULL OR t.budget <= :highBudget) " +
            "AND (:remoteTask is NULL OR t.remoteTask = :remoteTask) " +
            "AND (:categoryPublicIds is NULL OR t.taskCategory.publicId in :categoryPublicIds) " +
            "AND (:searchRemote = true OR function('calculate_distance', :latitude, ta.lat, :longitude, ta.lng, 'K') <= :radius)",
            value = "SELECT t FROM Task t " +
            "LEFT JOIN FETCH t.taskCategory tc " +
            "LEFT JOIN FETCH t.tasker tt " +
            "LEFT JOIN FETCH t.poster tp " +
            "LEFT JOIN FETCH t.userBids ub " +
            "LEFT JOIN FETCH t.userAddress ta " +
            "WHERE (lower(t.taskStatus) like lower('%APPROVED%') ) " +
            "AND (t.isDeleted = false) " +
            "AND t.poster <> :user " +
            "AND (:lowBudget is NULL OR t.budget >= :lowBudget) " +
            "AND (:highBudget is NULL OR t.budget <= :highBudget) " +
            "AND (:remoteTask is NULL OR t.remoteTask = :remoteTask) " +
            "AND ((:categoryPublicIds) is NULL OR t.taskCategory.publicId in (:categoryPublicIds)) " +
            "AND (:searchRemote = true OR function('calculate_distance', :latitude, ta.lat, :longitude, ta.lng, 'K') <= :radius)")
    Page<Task> searchTasks(List<Long> categoryPublicIds, BigInteger lowBudget, BigInteger highBudget, Boolean remoteTask, float longitude, float latitude, Long radius, Boolean searchRemote, User user, Pageable page);

    @Query(value = "SELECT t FROM Task t LEFT JOIN FETCH t.taskCategory tc LEFT JOIN FETCH t.tasker tt LEFT JOIN FETCH t.poster tp LEFT JOIN FETCH t.userBids ub WHERE t.isDeleted = false AND t.isApproved = true AND t.tasker = :bidder ORDER BY t.updateDateTime, t.createDateTime DESC")
    Collection<Task> getAllBidderTasks(User bidder);

    @Query(value = "SELECT COUNT(t) FROM Task t WHERE t.isDeleted = false AND t.isApproved = true AND t.tasker = :user AND lower(t.taskStatus) like lower('%COMPLETED%')")
    int findCountOfTasksCompletedByUserAsTasker(User user);

    @Query(value = "SELECT COUNT(t) FROM Task t WHERE t.isDeleted = false AND t.isApproved = true AND t.poster = :user AND lower(t.taskStatus) like lower('%COMPLETED%')")
    int findCountOfTasksCompletedByUserAsPoster(User user);
}
