package com.app.middleware.resources.services.implementation;

import com.app.middleware.persistence.domain.RolePermission;
import com.app.middleware.persistence.repository.RolePermissionRepository;
import com.app.middleware.resources.services.RolePermissionService;
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
