package com.bot.middleware.persistence.domain;

import lombok.*;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;
import org.json.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
//@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "user_addresses", schema = "public")
public class UserAddress extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name ="id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(updatable = false, unique = true, nullable = false, name ="public_id")
    private Long publicId;

    @Column(nullable = false, name ="address_line")
    private String addressLine;

    @Column(nullable = false, name ="apartment_address")
    private String apartmentAddress;

    @Column(nullable = false, name ="is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(nullable = false, name ="address_type")
    private String addressType;

    @Column(nullable = false, name ="country")
    private String country;

    @Column(nullable = false, name ="state")
    private String state;

    @Column(nullable = false, name ="city")
    private String city;

    @Column(nullable = false, name ="postal_code")
    private String postalCode;

    @Column(name ="lat")
    private float  lat;

    @Column(name ="lng")
    private float  lng;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Builder.Default
    @JoinColumn(name="user_id", referencedColumnName="public_id")
    private User user;

    public UserAddress(){ }
}
