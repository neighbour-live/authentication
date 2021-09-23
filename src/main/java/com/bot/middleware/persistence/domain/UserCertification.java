package com.bot.middleware.persistence.domain;
import com.bot.middleware.persistence.type.SkillProficiency;
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
@Table(name = "user_certifications", schema = "public")
public class UserCertification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name ="id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(updatable = false, unique = true, nullable = false, name ="public_id")
    private Long publicId;

    @Column(nullable = false, name ="title")
    private String title;

    @Column(nullable = false, name ="description")
    private String description;

    @Column(nullable = false, name ="issuing_institution")
    private String issuingInstitution;

    @Column(nullable = false, name ="issuing_date")
    private String issuingDate;

    @Column(name ="expiry_date")
    private String expiryDate;

    @Column(name ="certification_url")
    private String certificationURL;

    @Column(nullable = false, name ="is_approved")
    @Builder.Default
    private Boolean isApproved = false;

    @Column(nullable = false, name ="is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName="public_id")
    private User user;

    public UserCertification() { }
}