package com.bot.middleware.persistence.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
@AllArgsConstructor
@Builder
@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "task_timeline", schema = "public")
public class TaskTimeline extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(updatable = false, unique = true, nullable = false, name = "public_id")
    private Long publicId;

    @Column(nullable = false, name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(nullable = false, name ="is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "status")
    private String taskStatus;

    @Column(name = "status_change_time")
    private ZonedDateTime statusChangeTime;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="tasker_id",  referencedColumnName="public_id")
    private User tasker;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="poster_id",  referencedColumnName="public_id")
    private User poster;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="task_id",  referencedColumnName="public_id")
    private Task task;

    public TaskTimeline(){}
}
