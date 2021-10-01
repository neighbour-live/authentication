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

    @Column(unique = true, name ="email")
    private String email;

    @Column(unique = true, name ="user_name")
    private String userName;

    @Column(name ="password")
    private String password;

    @Column(unique = true, name ="phoneNumber")
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Builder.Default
    @JoinColumn(name="user_public_id", referencedColumnName="public_id")
    private User user;

    public UserTemporary() {}
}
