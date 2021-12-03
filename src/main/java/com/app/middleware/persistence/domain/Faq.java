package com.app.middleware.persistence.domain;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
@AllArgsConstructor
@Builder
@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "awards", schema = "public")
public class Faq extends BaseEntity  implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name ="id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(updatable = false, unique = true, nullable = false, name ="public_id")
    private Long publicId;

    @Column(nullable = false, name ="question")
    private String question;

    @Column(nullable = false, name ="description")
    private String description;

    @Column(nullable = false, name ="is_active")
    @Builder.Default
    private Boolean isActive = true;

    public Faq() {

    }
}
