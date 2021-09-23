package com.bot.middleware.persistence.domain;

import com.bot.middleware.persistence.type.AwardType;
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
@Table(name = "skills", schema = "public")
public class Skill extends BaseEntity  implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name ="id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(updatable = false, unique = true, nullable = false, name = "public_id")
    private Long publicId;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(name = "skill_icon")
    private String skillIcon;

    @Column(nullable = false, name ="is_approved")
    @Builder.Default
    private Boolean isApproved = false;

    public Skill() {

    }
}
