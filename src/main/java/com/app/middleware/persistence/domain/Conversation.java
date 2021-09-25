package com.app.middleware.persistence.domain;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="first_user_id",  referencedColumnName="public_id")
    private User firstUser;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="second_user_id",  referencedColumnName="public_id")
    private User secondUser;


    public Conversation() {}
}