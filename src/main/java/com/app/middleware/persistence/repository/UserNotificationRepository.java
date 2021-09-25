package com.app.middleware.persistence.repository;

import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    
    UserNotification findByPublicId(Long publicId);


    @Query(value = "SELECT un FROM UserNotification un " +
            "WHERE un.isDeleted = false " +
            "AND un.isArchived = false " +
            "AND un.isRead = false " +
            "AND un.user = :user"
    )
    List<UserNotification> findAllUserUnreadNotifications(User user);

    @Query(value = "SELECT un FROM UserNotification un " +
            "WHERE un.isDeleted = false " +
            "AND un.isArchived = false " +
            "AND un.isRead = true " +
            "AND un.user = :user"
    )
    List<UserNotification> findAllUserReadNotifications(User user);


    @Query(value = "SELECT un FROM UserNotification un " +
            "WHERE un.isDeleted = false " +
            "AND un.isArchived = true " +
            "AND un.user = :user"
    )
    List<UserNotification> findAllUserArchivedNotifications(User user);


    @Query(value = "SELECT un FROM UserNotification un " +
            "WHERE un.isDeleted = false " +
            "AND un.isArchived = false " +
            "AND un.user = :user"
    )
    List<UserNotification> findAllUserUnArchivedNotifications(User user);


    @Query( countQuery = "SELECT COUNT (un) FROM UserNotification un " +
            "WHERE un.user = :user AND un.isDeleted = false " +
            "AND un.isArchived = false "
            , value = "SELECT un FROM UserNotification un " +
            "WHERE un.user = :user AND un.isDeleted = false " +
            "AND un.isArchived = false "
    )
    Page<UserNotification> getUserNotificationsPage(Pageable pageable, User user);


    @Query( countQuery = "SELECT COUNT (un) FROM UserNotification un " +
            "WHERE un.user = :user AND un.isDeleted = false " +
            "AND un.isArchived = true "
            , value = "SELECT un FROM UserNotification un " +
            "WHERE un.user = :user AND un.isDeleted = false " +
            "AND un.isArchived = true "
    )
    Page<UserNotification> getUserNotificationsPageArchived(Pageable pageable, User user);


    @Query( countQuery = "SELECT COUNT (un) FROM UserNotification un " +
            "WHERE un.user = :user AND un.isDeleted = false " +
            "AND un.isArchived = false " +
            "AND (un.createDateTime BETWEEN :fromDate AND :toDate)"
            , value = "SELECT un FROM UserNotification un " +
            "WHERE un.user = :user AND un.isDeleted = false " +
            "AND un.isArchived = false " +
            "AND (un.createDateTime BETWEEN :fromDate AND :toDate)"
    )
    Page<UserNotification> getUserNotificationsPageNew(Pageable pageable, User user, ZonedDateTime fromDate, ZonedDateTime toDate);


    @Query(countQuery = "SELECT COUNT (un) FROM UserNotification un " +
            "WHERE un.user = :user  AND un.isDeleted = false " +
            "AND un.isArchived = false " +
            "AND un.createDateTime < :fromDate "
            , value = "SELECT un FROM UserNotification un " +
            "WHERE un.user = :user AND un.isDeleted = false " +
            "AND un.isArchived = false " +
            "AND un.createDateTime < :fromDate "
    )
    Page<UserNotification> getUserNotificationsPageEarlier(Pageable pageable, User user, ZonedDateTime fromDate);


    @Query( value = "SELECT COUNT (un) FROM UserNotification un " +
            "WHERE un.user = :user AND un.isDeleted = false " +
            "AND un.isArchived = false " +
            "AND un.isRead = false " +
            "AND (un.createDateTime BETWEEN :fromDate and :toDate) "
    )
    Integer getUserNotificationsNewUnreadCount(User user, ZonedDateTime fromDate, ZonedDateTime toDate);


    @Query(value = "SELECT COUNT (un) FROM UserNotification un " +
            "WHERE un.user = :user " +
            "AND un.isDeleted = false " +
            "AND un.isArchived = true " +
            "AND un.isRead = false"
    )
    Integer getUserNotificationsArchivedUnreadCount(User user);


    @Query( value = "SELECT COUNT (un) FROM UserNotification un " +
            "WHERE un.user = :user  AND un.isDeleted = false " +
            "AND un.isArchived = false " +
            "AND un.isRead = false " +
            "AND un.createDateTime < :fromDate "
    )
    Integer getUserNotificationsEarlierUnreadCount(User user, ZonedDateTime fromDate);
}
