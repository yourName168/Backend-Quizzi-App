package com.lth.identify_service.mapper;

import com.lth.identify_service.dto.request.PermissionRequest;
import com.lth.identify_service.dto.response.PermissionResponse;
import com.lth.identify_service.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
