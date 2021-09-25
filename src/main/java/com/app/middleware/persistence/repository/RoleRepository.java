package com.app.middleware.persistence.repository;

import com.app.middleware.persistence.domain.Role;
import com.app.middleware.persistence.type.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByRoleType(RoleType roleType);
}
