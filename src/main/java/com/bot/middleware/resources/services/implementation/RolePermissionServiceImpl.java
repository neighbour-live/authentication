package com.bot.middleware.resources.services.implementation;

import com.bot.middleware.persistence.domain.RolePermission;
import com.bot.middleware.persistence.repository.RolePermissionRepository;
import com.bot.middleware.resources.services.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Override
    public List<RolePermission> getRolePermissionsByRole(Long roleId) {
        return rolePermissionRepository.getRolePermissionsByRoleId(roleId);
    }
}
