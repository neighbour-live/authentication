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
@Table(name = "support_user", schema = "public")
public class SupportUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name ="id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(updatable = false, unique = true, nullable = false, name = "public_id")
    private Long publicId;

    @Column(nullable = false, name ="is_resolved")
    @Builder.Default
    private Boolean isResolved = false;

    @Column(nullable = false, name = "related_to")
    private String relatedTo;

    @Column(nullable = false, name = "description")
    private String description;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="user_public_id",  referencedColumnName="public_id")
    private User user;

    public SupportUser() {

    }
}
