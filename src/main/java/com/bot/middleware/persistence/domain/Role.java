package com.bot.middleware.persistence.domain;

import com.bot.middleware.persistence.type.RoleType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
//@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "role", schema = "public")
public class Role extends BaseEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name ="id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, name ="role_name")
    private String roleName;

    @Column(nullable = false, name ="is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @OneToMany(cascade = CascadeType.PERSIST , mappedBy = "role", fetch = FetchType.LAZY)
    @Builder.Default
    private Collection<RolePermission> rolePermissions = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name ="role_type")
    private RoleType roleType;

    public Role() {

    }

}
