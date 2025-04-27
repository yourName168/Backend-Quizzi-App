package com.lth.identify_service.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lth.identify_service.dto.request.RoleRequest;
import com.lth.identify_service.dto.response.RoleResponse;
import com.lth.identify_service.mapper.RoleMapper;
import com.lth.identify_service.repository.PermissionRepository;
import com.lth.identify_service.repository.RoleRepository;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);

    }

    public List<RoleResponse> getAll() {
        var roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    public void delete(String roleName) {
        roleRepository.deleteById(roleName);
    }
}
