package com.app.middleware.persistence.domain;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
//@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "user_temporary", schema = "public")
public class UserTemporary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name ="id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true, updatable = false, nullable = false ,name="public_id")
    private Long publicId;

    @Column(unique = true, name ="email")
    private String email;

    @Column(name ="email_code")
    private String emailCode;

    @Column(name ="email_token")
    private String emailToken;

    @Column(unique = true, name ="user_name")
    private String userName;

    @Column(name ="password")
    private String password;

    @Column(unique = true, name ="phoneNumber")
    private String phoneNumber;

    @Column(name ="phone_code")
    private String phoneCode;

    @Column(name ="phone_token")
    private String phoneToken;

    @Column(name ="email_verified")
    @Builder.Default
    private Boolean emailVerified = false;

    @Column(name ="phone_verified")
    @Builder.Default
    private Boolean phoneVerified = false;

    public UserTemporary() {}
}
