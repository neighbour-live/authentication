package com.app.middleware.resources.services;

import com.app.middleware.persistence.domain.RolePermission;

import java.util.List;

public interface RolePermissionService {

    List<RolePermission> getRolePermissionsByRole(Long roleId);
}
