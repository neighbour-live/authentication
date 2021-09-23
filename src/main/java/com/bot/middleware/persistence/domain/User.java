package com.bot.middleware.persistence.domain;
import com.bot.middleware.persistence.type.AuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
//@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "users", schema = "public")
public class User extends BaseEntity  implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name ="id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(updatable = false, unique = true, name ="public_id")
    private Long publicId;

    @Column(name ="stripe_id", unique = true)
    private String stripeId;

    @Column(name ="first_name")
    private String firstName;

    @Column(name ="last_name")
    private String lastName;

    @Email
    @Column(name ="email", unique = true)
    private String email;

    @Column(name ="connect_id", unique = true)
    private String connectId;

    @Column(name ="phone_number", unique = true)
    private String phoneNumber;

    @Column(name ="email_verified")
    @Builder.Default
    private Boolean emailVerified = false;

    @Column(name ="phone_verified")
    @Builder.Default
    private Boolean phoneVerified = false;

    @Column(name ="card_verified")
    @Builder.Default
    private Boolean cardVerified = false;

    @Column(name ="sterling_background_verified")
    @Builder.Default
    private Boolean sterlingBackgroundVerified = false;

    @Column(name ="is_blocked")
    @Builder.Default
    private Boolean isBlocked = false;

    @Column(name ="is_suspended")
    @Builder.Default
    private Boolean isSuspended = false;

    @Column(name ="is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name ="image_url")
    private String imageUrl;

    @JsonIgnore
    @Column(name ="password")
    private String password;

    @Column(unique = true, name ="access_token")
    private String accessToken;

    @Column(unique = true, name ="refresh_token")
    private String refreshToken;

    @Column(unique = true, name ="email_verification_token")
    private String emailVerificationToken;

    @Column(name = "email_token_creation_time")
    @UpdateTimestamp
    private ZonedDateTime emailTokenCreationTime;


    @Column(unique = true, name ="phone_verification_token")
    private String phoneVerificationToken;

    @Column(name ="phone_verification_otp")
    private String phoneVerificationOTP;

    @Column(name = "otp_creation_time")
    @UpdateTimestamp
    private ZonedDateTime otpCreationTime;

    @Column(name ="credentials_try_count")
    private Integer credentialsTryCount = 0;

    @Column(name ="tasks_completed_as_tasker")
    private Integer tasksCompletedAsTasker;

    @Column(name ="tasks_completed_as_poster")
    private Integer tasksCompletedAsPoster;

    @Enumerated(EnumType.STRING)
    @Column(name ="provider")
    private AuthProvider provider;

    @Column(name ="provider_id")
    private String providerId;

    @Column(name ="address_line")
    private String addressLine;

    @Column(name ="apartment_address")
    private String  apartmentAddress;

    @Column(name ="postal_code")
    private String  postalCode;

    @Column(name ="city")
    private String  city;

    @Column(name ="state")
    private String  state;

    @Column(name ="country")
    private String  country;

    @Column(name ="country_short")
    private String  country_short;

    @Column(name ="currency")
    private String  currency;

    @Column(name ="lat")
    private float  lat;

    @Column(name ="lng")
    private float  lng;

    @Column(name ="ip")
    private String  ip;

    @Column(name ="dob")
    private String  dob;

    @Column(name ="user_tagline")
    private String  tagLine;

    @Column(name ="user_bio")
    private String  bio;

    @Column(name ="address_type")
    private String addressType;

    @Column(name ="poster_rating_avg")
    private Double posterRatingAvg;

    @Column(name ="tasker_rating_avg")
    private Double taskerRatingAvg;

    @Column(name = "firebase_key")
    private String firebaseKey;

//    The cascade types supported by the Java Persistence Architecture are as below:
//
//    CascadeType.PERSIST : cascade type presist means that save() or persist() operations cascade to related entities.
//    CascadeType.MERGE : cascade type merge means that related entities are merged when the owning entity is merged.
//    CascadeType.REFRESH : cascade type refresh does the same thing for the refresh() operation.
//    CascadeType.REMOVE : cascade type remove removes all related entities association with this setting when the owning entity is deleted.
//    CascadeType.DETACH : cascade type detach detaches all related entities if a “manual detach” occurs.
//    CascadeType.ALL : cascade type all is shorthand for all of the above cascade operations.

    @OneToMany(cascade = CascadeType.PERSIST , mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private Collection<UserPaymentCard> userPaymentCards = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST , mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private Collection<UserBankAccount> userBankAccounts = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST , mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private Collection<UserAddress> userAddresses = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST , mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private Collection<UserAward> userAwards = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST , mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private Collection<UserCertification> userCertifications = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST , mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private Collection<UserSkill> userSkills = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST , mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private Collection<UserTransactions> userTransactions = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST , mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private Collection<UserBid> userBids = new ArrayList<>();

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="role_id",  referencedColumnName="id")
    private Role role;

    public User(){}
}
