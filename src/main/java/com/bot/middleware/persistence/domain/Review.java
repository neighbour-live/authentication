package com.bot.middleware.persistence.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
//@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "reviews", schema = "public")
public class Review extends BaseEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(updatable = false, unique = true, nullable = false, name = "public_id")
    private Long publicId;

    @Column(nullable = false, name = "tasker_name")
    private String taskerName;

    @Column(nullable = false, name = "poster_name")
    private String posterName;

    @Column(nullable = false, name = "tasker_image")
    private String taskerImage;

    @Column(nullable = false, name = "poster_image")
    private String posterImage;

    @Column(name = "tasker_review")
    private String taskerReview;

    @Column(name = "poster_review")
    private String posterReview;

    @Column(name = "tasker_stars")
    private Integer taskerStars;

    @Column(name = "poster_stars")
    private Integer posterStars;

    @Column(nullable = false, name ="is_reviewed_tasker")
    @Builder.Default
    private Boolean isReviewedTasker = false;

    @Column(nullable = false, name ="is_reviewed_poster")
    @Builder.Default
    private Boolean isReviewedPoster = false;

    @Column(nullable = false, name ="is_removed")
    @Builder.Default
    private Boolean isRemoved = false;

    @Column(nullable = false, name ="is_reported_by_tasker")
    @Builder.Default
    private Boolean isReportedByTasker = false;

    @Column(nullable = false, name ="is_reported_by_poster")
    @Builder.Default
    private Boolean isReportedByPoster = false;

    @Column(nullable = false, name ="is_deleted_by_tasker")
    @Builder.Default
    private Boolean isDeletedTasker = false;

    @Column(nullable = false, name ="is_deleted_by_poster")
    @Builder.Default
    private Boolean isDeletedPoster = false;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name="tasker_public_id",  referencedColumnName="public_id")
    private User tasker;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name="poster_public_id",  referencedColumnName="public_id")
    private User poster;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name="task_public_id",  referencedColumnName="public_id")
    private Task task;

    public Review(){}
}
