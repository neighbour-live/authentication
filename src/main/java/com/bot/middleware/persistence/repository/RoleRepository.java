package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.Role;
import com.bot.middleware.persistence.type.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByRoleType(RoleType roleType);
}
