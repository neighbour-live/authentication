package com.bot.middleware.persistence.domain;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
@AllArgsConstructor
@Builder
@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "conversations", schema = "public")
public class Conversation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name ="id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(updatable = false, unique = true, nullable = false, name ="public_id")
    private Long publicId;

    @Column(nullable = false, name ="is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(nullable = false, name ="is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(nullable = false, name ="description")
    private String description;

    @Column(nullable = false, name ="title")
    private String title;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="tasker_public_id",  referencedColumnName="public_id")
    private User tasker;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="poster_public_id",  referencedColumnName="public_id")
    private User poster;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="task_public_id",  referencedColumnName="public_id")
    private Task task;

    public Conversation() {}
}