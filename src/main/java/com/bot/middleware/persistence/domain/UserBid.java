package com.bot.middleware.persistence.domain;

import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
//@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "user_bids", schema = "public")
public class UserBid extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(updatable = false, unique = true, nullable = false, name = "public_id")
    private Long publicId;

    @Column(nullable = false, name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(nullable = false, name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "hours")
    private int hours;

    @Column(name = "hourly_rate")
    private Integer hourlyRate;

    @Column(nullable = false, name = "budget")
    private long budget;

    @Column(name = "other_costs")
    private long otherCosts = 0L;

    @Column( name = "description")
    private String description = "";

    @Column(name = "other_costs_explanation")
    private String otherCostsExplanation = "";

    @Column(name = "time_utilization_explanation")
    private String timeUtilizationExplanation = "";

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "status")
    private String status;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Builder.Default
    @JoinColumn(name = "user_id", referencedColumnName = "public_id")
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Builder.Default
    @JoinColumn(name = "task_id", referencedColumnName = "public_id")
    private Task task;

    public UserBid() {}
}
