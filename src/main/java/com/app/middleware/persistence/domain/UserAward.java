package com.app.middleware.persistence.domain;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
@AllArgsConstructor
@Builder
@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "user_awards", schema = "public")
public class UserAward extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name ="id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(updatable = false, unique = true, nullable = false, name ="public_id")
    private Long publicId;

    @Column(nullable = false, name ="is_active")
    @Builder.Default
    private Boolean isActive = false;

    @Column(nullable = false, name ="is_unlocked")
    @Builder.Default
    private Boolean isUnlocked = false;

    @Column(nullable = false, name ="progress")
    private Integer progress = 0;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Builder.Default
    @JoinColumn(name="award_id", referencedColumnName="public_id")
    private Award award;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Builder.Default
    @JoinColumn(name="user_id", referencedColumnName="public_id")
    private User user;

    public UserAward(){ }
}