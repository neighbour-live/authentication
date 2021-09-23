package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.Permission;
import com.bot.middleware.persistence.domain.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RolePermissionRepository  extends JpaRepository<RolePermission,Long> {

    @Query(value = "SELECT r FROM RolePermission r WHERE r.role.id = :roleId")
    List<RolePermission> getRolePermissionsByRoleId(@Param("roleId") Long roleId);
}
