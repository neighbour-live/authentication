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
@Table(name = "user_bank_account", schema = "public")
public class UserBankAccount extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name ="id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(updatable = false, unique = true, nullable = false, name ="public_id")
    private Long publicId;

    @Column(nullable = false, name ="account_holder_name")
    private String accountHolderName;

    @Column(nullable = false, name ="stripe_source_id")
    private String stripeSourceId;

    @Column(nullable = false, name ="stripe_source_object")
    private String stripeSourceObject;

    @Column(nullable = false, name ="bank_name")
    private String bankName;

    @Column(nullable = false, name ="transit_number")
    private String transitNumber;

    @Column(nullable = false, name ="financial_institution_number")
    private String financialInstitutionNumber;

    @Column(nullable = false, name ="account_number")
    private String accountNumber;

    @Column(nullable = false, name ="is_verified")
    @Builder.Default
    private Boolean isVerified = false;

    @Column(nullable = false, name ="is_active")
    @Builder.Default
    private Boolean isActive = false;

    @Column(nullable = false, name ="is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(nullable = false, name ="is_default")
    @Builder.Default
    private Boolean isDefault = false;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Builder.Default
    @JoinColumn(name="user_id", referencedColumnName="public_id")
    private User user;

    public UserBankAccount() { }
}
