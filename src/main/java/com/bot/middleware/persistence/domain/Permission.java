package com.bot.middleware.persistence.domain;

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
@Table(name = "permission", schema = "public")
public class Permission  extends BaseEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name ="id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, name ="permission_name")
    private String permissionName;

    @Column(nullable = false, name ="permission_code")
    private String permissionCode;

    @Column(nullable = false, name ="is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @OneToMany(cascade = CascadeType.PERSIST , mappedBy = "permission", fetch = FetchType.LAZY)
    @Builder.Default
    private Collection<RolePermission> rolePermissions = new ArrayList<>();


    public Permission() {

    }

}
