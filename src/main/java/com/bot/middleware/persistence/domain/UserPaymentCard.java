package com.bot.middleware.persistence.domain;
import com.bot.middleware.persistence.type.CardBrand;
import com.bot.middleware.persistence.type.CardType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
//@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "user_payment_cards", schema = "public")
public class UserPaymentCard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name ="id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(updatable = false, unique = true, nullable = false, name ="public_id")
    private Long publicId;

    @Column(nullable = false, name ="stripe_source_id")
    private String stripeSourceId;

    @Column(nullable = false, name ="connect_source_id")
    private String connectSourceId;

    @Column(nullable = false, name ="stripe_source_object")
    private String stripeSourceObject;

    @Column(nullable = false, name ="cardholder_name")
    private String cardholderName;

    @Column(unique = true, nullable = false, name ="card_number")
    private String cardNumber;

    @Column(nullable = false, name ="card_expiry_date")
    private String cardExpiryDate;

    @Column(nullable = false, name ="card_verified")
    @Builder.Default
    private Boolean cardVerified = false;

    @Column(nullable = false, name ="is_active")
    @Builder.Default
    private Boolean isActive = false;

    @Column(nullable = false, name ="is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(nullable = false, name ="is_default")
    @Builder.Default
    private Boolean isDefault = false;

    @Column(nullable = false, name ="card_type")
    private String cardType;

    @Column(nullable = false, name ="card_brand")
    private String cardBrand;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Builder.Default
    @JoinColumn(name="user_id", referencedColumnName="public_id")
    private User user;

    public UserPaymentCard() {}
}
