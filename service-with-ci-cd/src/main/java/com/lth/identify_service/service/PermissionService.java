package com.lth.identify_service.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lth.identify_service.dto.request.PermissionRequest;
import com.lth.identify_service.dto.response.PermissionResponse;
import com.lth.identify_service.entity.Permission;
import com.lth.identify_service.mapper.PermissionMapper;
import com.lth.identify_service.repository.PermissionRepository;


@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest permissionRequest) {
        Permission permission = permissionMapper.toPermission(permissionRequest);
        boolean exists = permissionRepository.existsById(permission.getName());
        if (exists) {
            throw new RuntimeException("Permission already exists");
        }
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).collect(Collectors.toList());
    }

    public void delete(String permissionName) {
        permissionRepository.deleteById(permissionName);
    }

}
