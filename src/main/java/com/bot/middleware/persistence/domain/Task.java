package com.bot.middleware.persistence.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
@AllArgsConstructor
@Builder
@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "tasks", schema = "public")
public class Task extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(updatable = false, unique = true, nullable = false, name = "public_id")
    private Long publicId;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "description")
    private String description;

    @Column(nullable = false, name ="is_approved")
    @Builder.Default
    private Boolean isApproved = false;

    @Column(nullable = false, name ="is_assigned")
    @Builder.Default
    private Boolean isAssigned = false;

    @Column(nullable = false, name ="is_pending")
    @Builder.Default
    private Boolean isPending = false;

    @Column(nullable = false, name ="is_completed")
    @Builder.Default
    private Boolean isCompleted = false;

    @Column(nullable = false, name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(nullable = false, name ="is_rescheduled")
    @Builder.Default
    private Boolean isRescheduled = false;

    @Column(nullable = false, name ="is_reschedule_requested")
    @Builder.Default
    private Boolean isRescheduleRequested = false;

    @Column(nullable = false, name = "start_date_time")
    private ZonedDateTime startDateTime;

    @Column(nullable = false, name = "end_date_time")
    private ZonedDateTime endDateTime;

    @Column(name = "task_repeat")
    String taskRepeat;

    @Column(nullable = false, name = "payment_type")
    String paymentType;

    @Column(name = "task_time")
    private Float taskTime;

    @Column(name = "reschedule_date_time")
    private ZonedDateTime rescheduleTaskTime;

    @Column(name = "hourly_rate")
    private Integer hourlyRate;

    @Column(name = "milestone_rate")
    private Integer milestoneRate;

    @Column(nullable = false, name = "budget")
    private BigInteger budget;

    @Column(name = "task_media")
    private String taskMedia;

    @Column(name = "task_status")
    private String taskStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="blocked_by",  referencedColumnName="public_id")
    private User blockedBy;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="buyer_public_id",  referencedColumnName="public_id")
    private User tasker;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="supplier_public_id",  referencedColumnName="public_id")
    private User poster;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_address_public_id",  referencedColumnName="public_id")
    private UserAddress userAddress;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="task_category_public_id",  referencedColumnName="public_id")
    private TaskCategories taskCategory;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="review_public_id",  referencedColumnName="public_id")
    private Review review;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="task_transaction",  referencedColumnName="public_id")
    private UserTransactions userTransactions;

    @OneToMany(cascade = CascadeType.PERSIST , mappedBy = "task", fetch = FetchType.LAZY)
    @Builder.Default
    private Collection<UserBid> userBids = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST , mappedBy = "task", fetch = FetchType.LAZY)
    @Builder.Default
    private Collection<UserTransactions> taskTransactions = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST , mappedBy = "task", fetch = FetchType.LAZY)
    @Builder.Default
    private Collection<TaskTimeline> taskTimelines = new ArrayList<>();

    @Column(nullable = false, name ="remote_task")
    @Builder.Default
    private Boolean remoteTask = false;

    public Task(){}
}
