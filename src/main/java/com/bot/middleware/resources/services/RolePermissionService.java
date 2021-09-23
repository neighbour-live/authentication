package com.bot.middleware.resources.services;

import com.bot.middleware.persistence.domain.RolePermission;

import java.util.List;

public interface RolePermissionService {

    List<RolePermission> getRolePermissionsByRole(Long roleId);
}
