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
@Table(name = "report_user", schema = "public")
public class ReportUser extends BaseEntity {
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

    @Column(nullable = false, name = "subject")
    private String subject;

    @Column(nullable = false, name = "issue")
    private String issue;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="reporter_public_id",  referencedColumnName="public_id")
    private User reporter;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="reported_public_id",  referencedColumnName="public_id")
    private User reported;

    public ReportUser() {

    }
}
