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
@Table(name = "charge", schema = "public")
public class Charge extends BaseEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name ="id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(updatable = false, unique = true, nullable = false, name ="public_id")
    private Long publicId;

    @Column(nullable = false, name ="stripe_charge_id")
    private String stripeChargeId;

    @Column(nullable = false, name ="title")
    private String title;

    @Column(nullable = false, name ="description")
    private String description;

    @Column(nullable = false, name ="internal_notes")
    private String internalNotes;

    @Column(nullable = false, name ="award_icon")
    private String serviceType;

    @Column(nullable = false, name ="transaction_type")
    private String transactionType;

    @Column(nullable = false, name ="payment_status")
    private String paymentStatus;

    @Column(nullable = false, name ="is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(nullable = false, name ="is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(nullable = false, name ="is_paid")
    @Builder.Default
    private Boolean isPaid = false;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Builder.Default
    @JoinColumn(name="payer_id", referencedColumnName="public_id")
    private User payer;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Builder.Default
    @JoinColumn(name="creator_id", referencedColumnName="public_id")
    private User creator;

    public Charge() {}

}
